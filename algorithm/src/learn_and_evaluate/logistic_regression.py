import json
from learn_and_evaluate.base_learn_and_evaluate import BaseLearnEvaluate
from data_loader import SmallBugReport, SmallSourceFile
import pickle
import os
import numpy as np
from scipy import optimize
from constant import *
from sklearn.linear_model import LogisticRegression


class LogisticRegressionLearner(BaseLearnEvaluate):
    def __init__(self, source_codes, bug_reports, rank_scores):
        super(LogisticRegressionLearner, self).__init__(source_codes, bug_reports, rank_scores)

    def build_dataset(self):
        x = []
        y = []
        for i, report in enumerate(self.bug_reports.items()):
            for j, code in enumerate(self.source_codes.items()):
                x.append([self.rank_scores[k][i][j] for k in range(len(self.rank_scores))])
                y.append(1 if code[0] in report[1].fixed_files else 0)
        return np.array(x), np.array(y)

    def combine_rank_scores(self, coef, intercept):
        final_score = []
        for scores in zip(*self.rank_scores):
            combined_score = coef @ np.array(scores) + intercept
            final_score.append(combined_score)
        return final_score

    def learn(self):
        x, y = self.build_dataset()
        lr = LogisticRegression(random_state=0)
        lr.fit(x, y)
        self.final_scores = self.combine_rank_scores(lr.coef_[0], lr.intercept_)


def do_and_save(path, scores_needed=SCORES_NEEDED_TO_LEARN,
                evaluation_result_file_name="",
                result_info=""):
    evaluation_result_file_name = evaluation_result_file_name.split(".")[0] + "-by-LR.txt"
    result_info = "Learn by Logistic Regression\nscores: " + str(scores_needed) + "\n\n" + result_info

    run_time_data_path = os.path.join(path, "run-time-data")
    output_path = os.path.join(path, "output")
    os.makedirs(output_path, exist_ok=True)
    evaluation_result_path = os.path.join(output_path, evaluation_result_file_name)
    # source_code_ranks_path = os.path.join(output_path, 'source-code-ranks.csv')

    small_source_code_path = os.path.join(run_time_data_path, "small-source-code.pickle")
    with open(small_source_code_path, 'rb') as file:
        small_source_codes = pickle.load(file)
    small_bug_report_path = os.path.join(run_time_data_path, "small-bug-report.pickle")
    with open(small_bug_report_path, 'rb') as file:
        small_bug_reports = pickle.load(file)

    rank_scores = []
    if "token_match" in scores_needed:
        token_matching_path = os.path.join(run_time_data_path, 'token-matching.json')
        with open(token_matching_path, 'r') as file:
            token_matching_score = json.load(file)
            rank_scores.append(token_matching_score)
    if "calculate_vsm_similarity" in scores_needed:
        vsm_similarity_path = os.path.join(run_time_data_path, 'vsm-similarity.json')
        with open(vsm_similarity_path, 'r') as file:
            vsm_similarity_score = json.load(file)
            rank_scores.append(vsm_similarity_score)
    if "calculate_stack_trace" in scores_needed:
        stack_trace_path = os.path.join(run_time_data_path, 'stack-trace.json')
        with open(stack_trace_path, 'r') as file:
            stack_trace_score = json.load(file)
            rank_scores.append(stack_trace_score)

    model = LogisticRegressionLearner(small_source_codes, small_bug_reports, rank_scores)
    model.learn()
    results = model.evaluate()

    bug_reports_num = len(small_bug_reports)
    string = "Top@1:\t{}/{} ({:.2f}%)\nTop@5:\t{}/{} ({:.2f}%)\nTop@10:\t{}/{} ({:.2f}%)\n" \
             "MRR:\t{:.2f}\nMAP:\t{:.2f}".format(results[0][0], bug_reports_num, results[1][0],
                                                 results[0][1], bug_reports_num, results[1][1],
                                                 results[0][2], bug_reports_num, results[1][2],
                                                 results[2],
                                                 results[3])

    with open(evaluation_result_path, "w") as file:
        file.write(result_info)
        file.write(string)


if __name__ == '__main__':
    do_and_save(path=NORMAL_SWT_PATH)
