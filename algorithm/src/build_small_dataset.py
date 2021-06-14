import pickle
from collections import OrderedDict
from sys import getsizeof
from constant import *


class SmallBugReport:
    def __init__(self, fixed_files):
        self.fixed_files = fixed_files


class SmallSourceFile:
    def __init__(self):
        pass


def do_and_save(path):
    run_time_data_path = os.path.join(path, "run-time-data")
    preprocessed_source_code_path = os.path.join(run_time_data_path, "preprocessed-source-code.pickle")
    preprocessed_bug_report_path = os.path.join(run_time_data_path, "preprocessed-bug-report.pickle")
    small_source_code_path = os.path.join(run_time_data_path, "small-source-code.pickle")
    small_bug_report_path = os.path.join(run_time_data_path, "small-bug-report.pickle")

    with open(preprocessed_source_code_path, 'rb') as file:
        source_codes = pickle.load(file)
    with open(preprocessed_bug_report_path, 'rb') as file:
        bug_reports = pickle.load(file)

    small_source_codes = OrderedDict()
    small_bug_reports = OrderedDict()

    for key in source_codes.keys():
        small_source_codes[key] = SmallSourceFile()
    for key in bug_reports.keys():
        small_bug_reports[key] = SmallBugReport(bug_reports[key].fixed_files)

    for source_code_key in small_source_codes.keys():
        for bug_report_key in small_bug_reports.keys():
            print(source_code_key, small_bug_reports[bug_report_key].fixed_files)

    with open(small_source_code_path, 'wb') as file:
        pickle.dump(small_source_codes, file, protocol=pickle.HIGHEST_PROTOCOL)
    with open(small_bug_report_path, 'wb') as file:
        pickle.dump(small_bug_reports, file, protocol=pickle.HIGHEST_PROTOCOL)


if __name__ == '__main__':
    do_and_save(LARGE_SWT_PATH)
