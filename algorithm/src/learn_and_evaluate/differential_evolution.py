import json
import operator
import pickle
from learn_and_evaluate.base_learn_and_evaluate import BaseLearnEvaluate
from data_loader import SmallBugReport, SmallSourceFile
import os
import numpy as np
from scipy import optimize
from constant import *


class DifferentialEvolutionLearner(BaseLearnEvaluate):
    def __init__(self, source_codes, bug_reports, rank_scores):
        super(DifferentialEvolutionLearner, self).__init__(source_codes, bug_reports, rank_scores)

    def combine_rank_scores(self, params):
        """
        通过params和分别的scores计算final score
        shape of rank_scores: (5, 98, 484)
        shape of *rank_scores: 5 * (98, 484)
        shape of zip(*rank_scores): 98 * (5, 484)

        shape of params: 5
        """

        final_score = []
        for scores in zip(*self.rank_scores):
            combined_score = params @ np.array(scores)
            final_score.append(combined_score)
        return final_score

    def cost(self, params):
        """
        最优化的目标函数，定义为MRR和mean_avgp的和的倒数
        shape of final_scores: (98, 484)
        """

        final_scores = self.combine_rank_scores(params)

        MRR = []
        mean_avgp = []

        for i, report in enumerate(self.bug_reports.items()):
            # report是一个二元组，分别是key和value

            # 对source codes根据score进行排序，将score高的文件排在前面
            src_ranks, _ = zip(*sorted(zip(self.source_codes.keys(), final_scores[i]),
                                       key=operator.itemgetter(1), reverse=True))

            # Getting reported fixed files
            fixed_files = report[1].fixed_files

            # Getting the ranks of reported fixed files
            relevant_ranks = sorted(src_ranks.index(fixed) + 1
                                    for fixed in fixed_files)
            # MRR
            min_rank = relevant_ranks[0]
            MRR.append(1 / min_rank)

            # MAP
            mean_avgp.append(np.mean([len(relevant_ranks[:j + 1]) / rank
                                      for j, rank in enumerate(relevant_ranks)]))

        return -1 * (np.mean(MRR) + np.mean(mean_avgp))


    def estimate_params(self):

        res = optimize.differential_evolution(
            self.cost, bounds=[(0, 1)] * len(self.rank_scores),
            args=(), strategy='randtobest1exp', polish=True, seed=458711526)

        return res.x.tolist()

    def learn(self):
        params = self.estimate_params()
        self.final_scores = self.combine_rank_scores(params)


def do_and_save(path, scores_needed=SCORES_NEEDED_TO_LEARN,
                evaluation_result_file_name="",
                result_info=""):
    evaluation_result_file_name = evaluation_result_file_name.split(".")[0] + "-by-DE.txt"
    result_info = "Learn by Differential Evolution\nscores: " + str(scores_needed) + "\n\n" + result_info

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

    model = DifferentialEvolutionLearner(small_source_codes, small_bug_reports, rank_scores)
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
