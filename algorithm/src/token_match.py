import json
import pickle
import time
import numpy as np
from sklearn import preprocessing
from constant import *


def token_matching(source_codes, bug_reports):
    scores = []
    for report in bug_reports.values():
        matched_count = []
        summary_set = report.summary
        pos_tagged_sum_desc = (report.pos_tagged_summary['stemmed'] +
                               report.pos_tagged_description['stemmed'])

        for source_code in source_codes.values():
            if source_code.file_name['stemmed']:
                common_tokens = len(set(summary_set['stemmed']) & set([source_code.file_name['stemmed'][0]]))
            matched_count.append(common_tokens)

        # Here no files matched a summary
        if sum(matched_count) == 0:
            matched_count = []
            for source_code in source_codes.values():
                common_tokens = len(set(pos_tagged_sum_desc) &
                                    set(source_code.file_name['stemmed'] +
                                        source_code.class_names['stemmed'] +
                                        source_code.method_names['stemmed']))

                if not common_tokens:
                    common_tokens = (len(set(pos_tagged_sum_desc) &
                                         set(source_code.comments['stemmed']))
                                     - len(set(source_code.comments['stemmed'])))

                if not common_tokens:
                    common_tokens = (len(set(pos_tagged_sum_desc) &
                                         set(source_code.attributes['stemmed']))
                                     - len(set(source_code.attributes['stemmed'])))

                matched_count.append(common_tokens)

        min_max_scaler = preprocessing.MinMaxScaler()

        intersect_count = np.array([float(count)
                                    for count in matched_count]).reshape(-1, 1)
        normalized_count = np.concatenate(
            min_max_scaler.fit_transform(intersect_count)
        )

        scores.append(normalized_count.tolist())

    return scores


def do_and_save(path=NORMAL_SWT_PATH):
    run_time_data_path = os.path.join(path, "run-time-data")
    preprocessed_source_code_path = os.path.join(run_time_data_path, "preprocessed-source-code.pickle")
    preprocessed_bug_report_path = os.path.join(run_time_data_path, "preprocessed-bug-report.pickle")
    token_matching_path = os.path.join(run_time_data_path, 'token-matching.json')

    # Unpickle preprocessed data
    with open(preprocessed_source_code_path, 'rb') as file:
        source_codes = pickle.load(file)
    with open(preprocessed_bug_report_path, 'rb') as file:
        bug_reports = pickle.load(file)

    scores = token_matching(source_codes, bug_reports)

    # Saving scores in a json file
    with open(token_matching_path, 'w') as file:
        json.dump(scores, file)


if __name__ == '__main__':
    print("calculate matching-scores of swt dataset...")
    begin_time = time.time()
    do_and_save(NORMAL_SWT_PATH)
    end_time = time.time()
    print("taking {:.2f}s".format(end_time - begin_time))
    # taking 0.27s

    # begin_time = time.time()
    # print("calculate matching-scores of swt dataset...")
    # calculate_matching_scores(LARGE_SWT_PATH)
    # end_time = time.time()
    # print("taking {:.2f}s".format(end_time - begin_time))
    # # taking 858.53s
    # print("calculating finish!")

