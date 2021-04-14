from concurrent import futures
import logging

import grpc

import preprocess_pb2
import preprocess_pb2_grpc


class PreProcessor(preprocess_pb2_grpc.PreProcessorServicer):

  def proto_preprocess(self, request, context):
    print(request.file_path)
    # todo
    return preprocess_pb2.PreProcessReply(succeed=1)


def serve():
  server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
  preprocess_pb2_grpc.add_PreProcessorServicer_to_server(PreProcessor(), server)
  server.add_insecure_port('[::]:50053')
  server.start()
  server.wait_for_termination()


if __name__ == '__main__':
  logging.basicConfig()
  serve()
