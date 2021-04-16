import pickle
import re
import string
import time
import inflection
import nltk
from nltk.stem.porter import PorterStemmer
from constant import *
from data_loader import DataLoader
from collections import OrderedDict


class Preprocessor:
    def _split_camelcase(self, tokens):
        # 识别并处理camelcase风格的变量名

        returning_tokens = tokens[:]        # 拷贝

        for token in tokens:
            # string.punctuation是英语中所有的标点字符组成的集合
            split_tokens = re.split(fr'[{string.punctuation}]+', token)

            if len(split_tokens) > 1:
                returning_tokens.remove(token)
                # Camel case detection for new tokens
                for st in split_tokens:
                    # inflection.underscore: transforms strings from CamelCase to underscored_string
                    camel_split = inflection.underscore(st).split('_')
                    if len(camel_split) > 1:
                        returning_tokens.append(st)
                        returning_tokens += camel_split
                    else:
                        returning_tokens.append(st)
            else:
                camel_split = inflection.underscore(token).split('_')
                if len(camel_split) > 1:
                    returning_tokens += camel_split

        return returning_tokens

    def _stem(self, tokens):
        stemmer = PorterStemmer()

        stemmed_tokens = []
        for token in tokens:
            stemmed_tokens.append(stemmer.stem(token))
        return {'stemmed': stemmed_tokens, 'unstemmed': tokens}

    def _normalize(self, tokens):
        returning_tokens = []
        remove_char = string.punctuation + string.digits
        for token in tokens:
            if token in remove_char:
                continue
            else:
                # str.maketrans是字符串映射
                token = token.translate(str.maketrans('', '', remove_char))
                if token == '':
                    continue
                returning_tokens.append(token.lower())
        return returning_tokens

    def _remove_stopwords(self, tokens):
        returning_tokens = []
        for token in tokens:
            if token not in STOP_WORDS:
                returning_tokens.append(token)
        return returning_tokens

    def _remove_java_keywords(self, tokens):
        returning_tokens = []
        for token in tokens:
            if token not in JAVA_KEYWORDS:
                returning_tokens.append(token)
        return returning_tokens

class BugReportPreprocessor(Preprocessor):

    def __init__(self, bug_reports):
        self.bug_reports = bug_reports
        self.attr_list = ['summary', 'description', 'pos_tagged_summary', 'pos_tagged_description']

    def extract_stack_traces(self):
        # stack_traces是指在report文件中出现java源代码的调用信息
        # 例如：
        #  at org.eclipse.swt.widgets.Table.callWindowProc(Table.java:156)
        pattern = re.compile(r' at (.*?)\((.*?)\)')

        # Signs of a true stack trace to check in the retrieved regex grouping
        signs = ['.java', 'Unknown Source', 'Native Method']

        for report in self.bug_reports.values():
            stack_traces = re.findall(pattern, report.description)

            # stack_traces的后半部分只能是java文件，或者Unknown Source和Native Method
            stack_traces = [x for x in stack_traces if any(s in x[1] for s in signs)]
            report.stack_traces = stack_traces

    def pos_tagging(self):
        # 对report进行词性标注，并将名词和动词提取出来
        # NN 名词
        # VB 动词
        for report in self.bug_reports.values():
            summary_tokenized = nltk.word_tokenize(report.summary)
            description_tokenized = nltk.word_tokenize(report.description)
            summary_tagged = nltk.pos_tag(summary_tokenized)
            description_tagged = nltk.pos_tag(description_tokenized)
            report.pos_tagged_summary = [token for token, pos in summary_tagged
                                         if 'NN' in pos or 'VB' in pos]
            report.pos_tagged_description = [token for token, pos in description_tagged
                                             if 'NN' in pos or 'VB' in pos]

    def tokenize(self):
        # 使用punkt句子分割器进行分词
        for report in self.bug_reports.values():
            report.summary = nltk.wordpunct_tokenize(report.summary)
            report.description = nltk.wordpunct_tokenize(report.description)

    def split_camelcase(self):
        # 将camelcase风格的变量名分开
        for report in self.bug_reports.values():
            for attr in self.attr_list:
                setattr(report, attr, self._split_camelcase(getattr(report, attr)))

    def normalize(self):
        # 去掉标点符号，小写转大写
        for report in self.bug_reports.values():
            for attr in self.attr_list:
                setattr(report, attr, self._normalize(getattr(report, attr)))

    def remove_stopwords(self):
        # 去掉停用词
        for report in self.bug_reports.values():
            for attr in self.attr_list:
                setattr(report, attr, self._remove_stopwords(getattr(report, attr)))

    def remove_java_keywords(self):
        # 去掉java关键字
        for report in self.bug_reports.values():
            for attr in self.attr_list:
                setattr(report, attr, self._remove_java_keywords(getattr(report, attr)))

    def stem(self):
        # 提取词干
        for report in self.bug_reports.values():
            for attr in self.attr_list:
                setattr(report, attr, self._stem(getattr(report, attr)))

    def preprocess(self):
        self.extract_stack_traces()
        self.pos_tagging()
        self.tokenize()
        self.split_camelcase()
        self.normalize()
        self.remove_stopwords()
        self.remove_java_keywords()
        self.stem()


class SourceCodePreprocessor(Preprocessor):
    def __init__(self, source_codes):
        self.source_codes = source_codes
        self.attr_list = ['all_content', 'comments', 'class_names', 'attributes', 'method_names', 'variables',
                          'file_name', 'pos_tagged_comments']

    def pos_tagging(self):
        for source_code in self.source_codes.values():
            comments_tokenized = nltk.word_tokenize(source_code.comments)
            comments_tagged = nltk.pos_tag(comments_tokenized)
            source_code.pos_tagged_comments = [token for token, pos in comments_tagged
                                               if 'NN' in pos or 'VB' in pos]

    def tokenize(self):
        for src in self.source_codes.values():
            src.all_content = nltk.wordpunct_tokenize(src.all_content)
            src.comments = nltk.wordpunct_tokenize(src.comments)

    def split_camelcase(self):
        for source_code in self.source_codes.values():
            for attr in self.attr_list:
                setattr(source_code, attr, self._split_camelcase(getattr(source_code, attr)))

    def normalize(self):
        for source_code in self.source_codes.values():
            for attr in self.attr_list:
                setattr(source_code, attr, self._normalize(getattr(source_code, attr)))

    def remove_stopwords(self):
        for source_code in self.source_codes.values():
            for attr in self.attr_list:
                setattr(source_code, attr, self._remove_stopwords(getattr(source_code, attr)))

    def remove_java_keywords(self):
        for source_code in self.source_codes.values():
            for attr in self.attr_list:
                setattr(source_code, attr, self._remove_java_keywords(getattr(source_code, attr)))

    def stem(self):
        for source_code in self.source_codes.values():
            for attr in self.attr_list:
                setattr(source_code, attr, self._stem(getattr(source_code, attr)))

    def preprocess(self):
        self.pos_tagging()
        self.tokenize()
        self.split_camelcase()
        self.normalize()
        self.remove_stopwords()
        self.remove_java_keywords()
        self.stem()


def load_preprocess_save(project_path, source_code_path=r"/data/source-code",
                         python_cache_path=r"/data/python-cache"):
    load_path = os.path.join(source_code_path, project_path)
    loader = DataLoader(load_path)

    source_codes = loader.load_source_code()
    preprocessor = SourceCodePreprocessor(source_codes)
    preprocessor.preprocess()

    for key in preprocessor.source_codes.keys():
        file_path = os.path.join(python_cache_path, project_path, key.split('.')[0] + ".pkl")
        dirname = os.path.split(file_path)[0]
        os.makedirs(dirname, exist_ok=True)
        with open(file_path, "wb") as f:
            pickle.dump(preprocessor.source_codes[key], f)



if __name__ == '__main__':
    load_preprocess_save(r"swt")

    # begin_time = time.time()
    # print("preprocessing large swt dataset...")
    # do_and_save(LARGE_SWT_PATH)
    # end_time = time.time()
    # print("taking {:.2f}s".format(end_time - begin_time))
    # # taking 11654.39s
    # print("preprocessing finish!")



