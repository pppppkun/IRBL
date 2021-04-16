from constant import *
import preprocess
from data_loader import SmallBugReport, SmallSourceFile
import token_match
import calculate_vsm_similarity
import calculate_stack_trace
import learn_and_evaluate.logistic_regression as logistic_regression
import learn_and_evaluate.differential_evolution as differential_evolution


def do_and_save(path, scores_needed_to_learn=SCORES_NEEDED_TO_LEARN,
                scores_needed_to_calculate=SCORES_NEEDED_TO_CALCULATE,
                data_preprocess_needed=False,
                evaluation_result_file_name='evaluation-result.txt',
                result_info="",
                learn_algorithm="logistic_regression"):
    if data_preprocess_needed:
        print("data preprocessing...")
        preprocess.do_and_save(path)

    if "token_match" in scores_needed_to_learn and "token_match" in scores_needed_to_calculate:
        print("token matching...")
        token_match.do_and_save(path)

    if "calculate_vsm_similarity" in scores_needed_to_learn and "calculate_vsm_similarity" in scores_needed_to_calculate:
        print("calculating vsm similarity...")
        calculate_vsm_similarity.do_and_save(path)

    if "calculate_stack_trace" in scores_needed_to_learn and "calculate_stack_trace" in scores_needed_to_calculate:
        print("calculating stack trace...")
        calculate_stack_trace.do_and_save(path)

    print("evaluating...")
    if learn_algorithm == "logistic_regression":
        learn_and_evaluate = logistic_regression
    elif learn_algorithm == "differential_evolution":
        learn_and_evaluate = differential_evolution
    else:
        learn_and_evaluate = None
        assert True, "no achieve yet"
    learn_and_evaluate.do_and_save(path, scores_needed=scores_needed_to_learn,
                                   evaluation_result_file_name=evaluation_result_file_name, result_info=result_info)


if __name__ == "__main__":
    do_and_save(NORMAL_SWT_PATH, learn_algorithm="differential_evolution")
    # scores_needed_to_learn = ["fix_bug_reports"]
    # do_and_save(LARGE_SWT_PATH, scores_needed_to_learn=scores_needed_to_learn)
