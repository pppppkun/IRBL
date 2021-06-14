from xml.dom.minidom import parseString
from collections import OrderedDict
from abc import ABC, abstractmethod
import javalang
import os
import multiprocessing as mp
import glob
import pygments
from pygments.lexers import JavaLexer
from pygments.token import Token
from constant import *


class BugReport:
    def __init__(self, summary, description, fixed_files=None):
        self.summary = summary
        self.description = description
        self.fixed_files = fixed_files

    def __str__(self):
        return "summary: {}\ndescription: {}\nfixed_files:{}".format(self.summary, self.description, self.fixed_files)


class SourceFile:
    def __init__(self, all_content, comments, class_names, attributes,
                 method_names, variables, file_name, package_name):
        self.all_content = all_content
        self.comments = comments
        self.class_names = class_names
        self.attributes = attributes
        self.method_names = method_names
        self.variables = variables
        self.file_name = file_name
        self.exact_file_name = file_name[0]
        self.package_name = package_name
        self.pos_tagged_comments = None

    def __str__(self):
        return "file_name: {}\nexact_file_name: {}\n" \
               "class_names: {}\nattributes: {}\nmethod_names: {}\n" \
               "variables: {}".format(
            self.exact_file_name,
            self.file_name,
            self.class_names,
            self.attributes,
            self.method_names,
            self.variables)


class DataLoader():
    def __init__(self, source_code_path):
        self.source_code_path = source_code_path

    def split_path(self, path):
        folders = []
        while 1:
            path, folder = os.path.split(path)
            if path != "":
                folders.append(folder)
            else:
                folders.append(folder)
                break
        folders.reverse()
        return folders

    def load_source_code(self):
        source_code_paths = glob.glob(str(self.source_code_path) + '/**/*.java', recursive=True)

        java_lexer = JavaLexer()

        source_codes = OrderedDict()

        for source_code_path in source_code_paths:

            with open(source_code_path) as f:
                source_code = f.read()

            comments = ''
            class_names = []
            attributes = []
            method_names = []
            variables = []

            # 使用javalang.parse.parse解析源文件
            source_code_tree = None
            try:
                source_code_tree = javalang.parse.parse(source_code)
                for path, node in source_code_tree.filter(javalang.tree.VariableDeclarator):
                    if isinstance(path[-2], javalang.tree.FieldDeclaration):
                        attributes.append(node.name)
                    elif isinstance(path[-2], javalang.tree.VariableDeclaration):
                        variables.append(node.name)
            except javalang.parser.JavaSyntaxError:
                print(source_code_path, " wrong")
                pass

            # 将源代码中的import和package部分去掉
            if source_code_tree:
                if source_code_tree.imports:
                    last_imp_path = source_code_tree.imports[-1].path
                    source_code = source_code[source_code.index(last_imp_path) +
                              len(last_imp_path) + 1:]
                elif source_code_tree.package:
                    package_name = source_code_tree.package.name
                    source_code = source_code[source_code.index(package_name) + len(package_name) + 1:]

            # Lexically tokenize the source file
            lexed_source_code = pygments.lex(source_code, java_lexer)

            for i, token in enumerate(lexed_source_code):
                if token[0] in Token.Comment:
                    if source_code_tree and i == 0 and token[0] is Token.Comment.Multiline:
                        source_code = source_code[source_code.index(token[1]) + len(token[1]):]
                        continue
                    comments += token[1]
                elif token[0] is Token.Name.Class:
                    class_names.append(token[1])
                elif token[0] is Token.Name.Function:
                    method_names.append(token[1])

            # Get the package declaration if exists
            if source_code_tree and source_code_tree.package:
                package_name = source_code_tree.package.name
            else:
                package_name = None

            source_code_id = "/".join(self.split_path(source_code_path[len(self.source_code_path) + 1:]))

            source_codes[source_code_id] = SourceFile(
                source_code, comments, class_names, attributes,
                method_names, variables,
                [os.path.basename(source_code_path).split('.')[0]],
                package_name
            )

        return source_codes

    def string_to_file_path(self, string):
        path_list = string.split(".")
        path_list[-2] = path_list[-2] + ".java"
        path_list.pop(-1)
        path = "/".join(path_list)
        return path

    def xml_file_replace(self, string):
        return string


if __name__ == "__main__":
    pass
