import json
import pickle
from collections import OrderedDict
from constant import *


def get_traces_score(src_files, bug_reports):

    all_file_names = set(s.exact_file_name for s in src_files.values())

    all_scores = []
    for report in bug_reports.values():

        scores = []

        stack_traces = report.stack_traces

        # Preprocessing stack-traces
        final_st = []
        for trace in stack_traces:
            if trace[1] == 'Unknown Source':
                final_st.append(
                    (trace[0].split('.')[-2].split('$')[0], trace[0].strip()))
            elif trace[1] != 'Native Method':
                final_st.append(
                    (trace[1].split('.')[0].replace(' ', ''), trace[0].strip()))

        stack_traces = OrderedDict([(file, package) for file, package in final_st
                                    if file in all_file_names])

        for src in src_files.values():
            file_name = src.exact_file_name

            # If the source file has a package name
            if src.package_name:
                if file_name in stack_traces and src.package_name in stack_traces[file_name]:
                    scores.append(
                        1 / (list(stack_traces).index(file_name) + 1))

                else:
                    # If it isn't the exact source file based on it's package name
                    scores.append(0)
            # If it doesn't have a package name
            elif file_name in stack_traces:
                scores.append(1 / (list(stack_traces).index(file_name) + 1))
            else:
                scores.append(0)

        all_scores.append(scores)

    return all_scores


def do_and_save(path):
    run_time_data_path = os.path.join(path, "run-time-data")
    preprocessed_source_code_path = os.path.join(run_time_data_path, "preprocessed-source-code.pickle")
    preprocessed_bug_report_path = os.path.join(run_time_data_path, "preprocessed-bug-report.pickle")
    stack_trace_path = os.path.join(run_time_data_path, 'stack-trace.json')

    with open(preprocessed_source_code_path, 'rb') as file:
        src_files = pickle.load(file)
    with open(preprocessed_bug_report_path, 'rb') as file:
        bug_reports = pickle.load(file)

    all_scores = get_traces_score(src_files, bug_reports)

    with open(stack_trace_path, 'w') as file:
        json.dump(all_scores, file)


if __name__ == '__main__':
    do_and_save(NORMAL_SWT_PATH)
