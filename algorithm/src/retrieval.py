from preprocess import BugReportPreprocessor
from data_loader import BugReport
import pickle
import glob
import os
from token_match import token_matching
from calculate_vsm_similarity import calculating_vsm_similarity
import numpy as np


PARMAS = [0.395404709534767, 0.7994527355369837]


def load_python_cache(project_path="swt", python_cache_path=r"C:\Users\10444\Desktop\se3\data\python-cache"):
    python_cache_paths = glob.glob(os.path.join(python_cache_path, project_path) + '/**/*.pkl', recursive=True)

    source_codes = {}
    for path in python_cache_paths:
        with open(path, "rb") as f:
            source_codes[path] = pickle.load(f)

    return source_codes


def file_retrieval(report_path, project_path, python_cache_path=r"C:\Users\10444\Desktop\se3\data\python-cache"):
    report_id = 0
    description = ""
    with open(report_path, "r") as f:
        summary = f.read()
    bug_report = BugReport(summary, description)
    bug_reports = {report_id: bug_report}
    preprocessor = BugReportPreprocessor(bug_reports)

    preprocessor.preprocess()

    source_codes = load_python_cache(project_path, python_cache_path)

    token_match_score = token_matching(source_codes, bug_reports)[0]
    vsm_similarity_score = calculating_vsm_similarity(source_codes, bug_reports)[0]

    final_score = PARMAS[0] * np.array(token_match_score) + PARMAS[1] * np.array(vsm_similarity_score)
    return [(item[0][len(python_cache_path):-4] + ".java", item[1])for item in sorted(zip(list(source_codes.keys()), final_score), key=lambda x: x[1], reverse=True)]
    # return list(sorted(zip(list(source_codes.keys()), final_score), key=lambda x: x[1], reverse=True))


if __name__ == "__main__":
    a = file_retrieval(r'C:\Users\10444\Desktop\se3\data\fuck.txt', "swt")
    print(a)
