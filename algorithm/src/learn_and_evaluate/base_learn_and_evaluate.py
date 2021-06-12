import json
import operator
from tqdm import tqdm
import pickle
import os
import numpy as np
from scipy import optimize
import sys
from constant import *

class BaseLearnEvaluate:
    def __init__(self, source_codes, bug_reports, rank_scores):
        self.source_codes = source_codes
        self.bug_reports = bug_reports
        self.rank_scores = rank_scores
        self.final_scores = None
        self.params = None


    def evaluate(self):
        assert self.final_scores is not None, "final_scores should be learn or set first"

        top_n = (1, 5, 10)
        top_n_rank = [0] * len(top_n)
        mrr = []
        mean_avgp = []

        precision_at_n = [[] for _ in top_n]
        recall_at_n = [[] for _ in top_n]
        f_measure_at_n = [[] for _ in top_n]

        for i, (bug_id, report) in tqdm(enumerate(self.bug_reports.items()), desc="evaluate",
                                        total=len(self.bug_reports)):
            # Finding source codes from the simis indices
            source_code_ranks, _ = zip(*sorted(zip(self.source_codes.keys(), self.final_scores[i]),
                                               key=operator.itemgetter(1), reverse=True))

            # Getting reported fixed files
            fixed_files = report.fixed_files

            # Iterating over top n
            for k, rank in enumerate(top_n):
                hit = set(source_code_ranks[:rank]) & set(fixed_files)

                # Computing top n rank
                if hit:
                    top_n_rank[k] += 1

                # Computing precision and recall at n
                if not hit:
                    precision_at_n[k].append(0)
                else:
                    precision_at_n[k].append(len(hit) / len(source_code_ranks[:rank]))
                recall_at_n[k].append(len(hit) / len(fixed_files))
                if not (precision_at_n[k][i] + recall_at_n[k][i]):
                    f_measure_at_n[k].append(0)
                else:
                    f_measure_at_n[k].append(2 * (precision_at_n[k][i] * recall_at_n[k][i])
                                             / (precision_at_n[k][i] + recall_at_n[k][i]))

            # Getting the ranks of reported fixed files
            relevant_ranks = sorted(source_code_ranks.index(fixed) + 1
                                    for fixed in fixed_files)
            # MRR
            min_rank = relevant_ranks[0]
            mrr.append(1 / min_rank)

            # MAP
            mean_avgp.append(np.mean([len(relevant_ranks[:j + 1]) / rank
                                      for j, rank in enumerate(relevant_ranks)]))

        return (top_n_rank, [x / len(self.bug_reports) for x in top_n_rank],
                np.mean(mrr), np.mean(mean_avgp),
                np.mean(precision_at_n, axis=1).tolist(), np.mean(
            recall_at_n, axis=1).tolist(),
                np.mean(f_measure_at_n, axis=1).tolist())

    def learn(self):
        pass
