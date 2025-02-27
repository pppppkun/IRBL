import json
import pickle

import numpy as np
from sklearn import preprocessing
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from constant import *


class Similarity:

    __slots__ = ['src_files', 'src_strings']

    def __init__(self, src_files):
        self.src_files = src_files
        self.src_strings = [' '.join(src.file_name['stemmed'] + src.class_names['stemmed']
                                     + src.method_names['stemmed']
                                     + src.pos_tagged_comments['stemmed']
                                     + src.attributes['stemmed'])
                            for src in self.src_files.values()]

    def calculate_similarity(self, src_tfidf, reports_tfidf):
        """Calculatnig cosine similarity between source files and bug reports"""

        # Normalizing the length of source files
        src_lenghts = np.array([float(len(src_str.split()))
                                for src_str in self.src_strings]).reshape(-1, 1)
        min_max_scaler = preprocessing.MinMaxScaler()
        normalized_src_len = min_max_scaler.fit_transform(src_lenghts)

        # Applying logistic length function
        src_len_score = 1 / (1 + np.exp(-12 * normalized_src_len))

        simis = []
        for report in reports_tfidf:
            s = cosine_similarity(src_tfidf, report)

            # revised VSM score calculation
            rvsm_score = s * src_len_score

            normalized_score = np.concatenate(
                min_max_scaler.fit_transform(rvsm_score)
            )

            simis.append(normalized_score.tolist())

        return simis

    def find_similars(self, bug_reports):
        """Calculating tf-idf vectors for source and report sets
        to find similar source files for each bug report.
        """

        reports_strings = [' '.join(report.summary['stemmed'] + report.description['stemmed'])
                           for report in bug_reports.values()]

        tfidf = TfidfVectorizer(sublinear_tf=True, smooth_idf=False)
        src_tfidf = tfidf.fit_transform(self.src_strings)

        reports_tfidf = tfidf.transform(reports_strings)

        simis = self.calculate_similarity(src_tfidf, reports_tfidf)
        return simis

def calculating_vsm_similarity(source_codes, bug_reports):
    sm = Similarity(source_codes)
    similarity = sm.find_similars(bug_reports)
    return similarity


def do_and_save(path):
    run_time_data_path = os.path.join(path, "run-time-data")
    preprocessed_source_code_path = os.path.join(run_time_data_path, "preprocessed-source-code.pickle")
    preprocessed_bug_report_path = os.path.join(run_time_data_path, "preprocessed-bug-report.pickle")
    vsm_similarity_path = os.path.join(run_time_data_path, 'vsm-similarity.json')

    # Unpickle preprocessed data
    with open(preprocessed_source_code_path, 'rb') as file:
        source_codes = pickle.load(file)
    with open(preprocessed_bug_report_path, 'rb') as file:
        bug_reports = pickle.load(file)

    sm = Similarity(source_codes)
    simis = sm.find_similars(bug_reports)

    # Saving similarities in a json file
    with open(vsm_similarity_path, 'w') as file:
        json.dump(simis, file)


if __name__ == '__main__':
    do_and_save(NORMAL_SWT_PATH)
