from concurrent import futures
import logging

import grpc

import calc_msg_pb2
import calc_msg_pb2_grpc

from retrieval import file_retrieval


class Calculator(calc_msg_pb2_grpc.CalculatorServicer):

    def calculate(self, request, context):
        # todo
        print(request.bug_report_path)
        print(request.file_path)
        # singleFileScore = calc_msg_pb2.FileScore(file_path=request.file_path, score=1)
        # return calc_msg_pb2.CalcReply(succeed=1, evaluation=[singleFileScore])
        fileScoreList_tuple = file_retrieval(request.bug_report_path, request.file_path)
        fileScores = []
        for fileScore in fileScoreList_tuple:
            fileScores.append(calc_msg_pb2.FileScore(file_path=fileScore[0], score=fileScore[1]))
        return calc_msg_pb2.CalcReply(succeed=1, evaluation=fileScores)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    calc_msg_pb2_grpc.add_CalculatorServicer_to_server(Calculator(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    logging.basicConfig()
    serve()
