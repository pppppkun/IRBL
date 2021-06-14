package pgd.irbl.business.serviceImpl.protobuf;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * 预处理服务定义
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.36.0)",
    comments = "Source: preprocess.proto")
public final class PreProcessorGrpc {

  private PreProcessorGrpc() {}

  public static final String SERVICE_NAME = "serviceImpl.PreProcessor";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<PreProcessRequest,
      PreProcessReply> getProtoPreprocessMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "proto_preprocess",
      requestType = PreProcessRequest.class,
      responseType = PreProcessReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<PreProcessRequest,
      PreProcessReply> getProtoPreprocessMethod() {
    io.grpc.MethodDescriptor<PreProcessRequest, PreProcessReply> getProtoPreprocessMethod;
    if ((getProtoPreprocessMethod = PreProcessorGrpc.getProtoPreprocessMethod) == null) {
      synchronized (PreProcessorGrpc.class) {
        if ((getProtoPreprocessMethod = PreProcessorGrpc.getProtoPreprocessMethod) == null) {
          PreProcessorGrpc.getProtoPreprocessMethod = getProtoPreprocessMethod =
              io.grpc.MethodDescriptor.<PreProcessRequest, PreProcessReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "proto_preprocess"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  PreProcessRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  PreProcessReply.getDefaultInstance()))
              .setSchemaDescriptor(new PreProcessorMethodDescriptorSupplier("proto_preprocess"))
              .build();
        }
      }
    }
    return getProtoPreprocessMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PreProcessorStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PreProcessorStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PreProcessorStub>() {
        @Override
        public PreProcessorStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PreProcessorStub(channel, callOptions);
        }
      };
    return PreProcessorStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PreProcessorBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PreProcessorBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PreProcessorBlockingStub>() {
        @Override
        public PreProcessorBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PreProcessorBlockingStub(channel, callOptions);
        }
      };
    return PreProcessorBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PreProcessorFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PreProcessorFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PreProcessorFutureStub>() {
        @Override
        public PreProcessorFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PreProcessorFutureStub(channel, callOptions);
        }
      };
    return PreProcessorFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 预处理服务定义
   * </pre>
   */
  public static abstract class PreProcessorImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * preprocess
     * </pre>
     */
    public void protoPreprocess(PreProcessRequest request,
                                io.grpc.stub.StreamObserver<PreProcessReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getProtoPreprocessMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getProtoPreprocessMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                PreProcessRequest,
                PreProcessReply>(
                  this, METHODID_PROTO_PREPROCESS)))
          .build();
    }
  }

  /**
   * <pre>
   * 预处理服务定义
   * </pre>
   */
  public static final class PreProcessorStub extends io.grpc.stub.AbstractAsyncStub<PreProcessorStub> {
    private PreProcessorStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected PreProcessorStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PreProcessorStub(channel, callOptions);
    }

    /**
     * <pre>
     * preprocess
     * </pre>
     */
    public void protoPreprocess(PreProcessRequest request,
                                io.grpc.stub.StreamObserver<PreProcessReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getProtoPreprocessMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * 预处理服务定义
   * </pre>
   */
  public static final class PreProcessorBlockingStub extends io.grpc.stub.AbstractBlockingStub<PreProcessorBlockingStub> {
    private PreProcessorBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected PreProcessorBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PreProcessorBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * preprocess
     * </pre>
     */
    public PreProcessReply protoPreprocess(PreProcessRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getProtoPreprocessMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * 预处理服务定义
   * </pre>
   */
  public static final class PreProcessorFutureStub extends io.grpc.stub.AbstractFutureStub<PreProcessorFutureStub> {
    private PreProcessorFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected PreProcessorFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PreProcessorFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * preprocess
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<PreProcessReply> protoPreprocess(
        PreProcessRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getProtoPreprocessMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PROTO_PREPROCESS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PreProcessorImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PreProcessorImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PROTO_PREPROCESS:
          serviceImpl.protoPreprocess((PreProcessRequest) request,
              (io.grpc.stub.StreamObserver<PreProcessReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PreProcessorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PreProcessorBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return PreProcessProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PreProcessor");
    }
  }

  private static final class PreProcessorFileDescriptorSupplier
      extends PreProcessorBaseDescriptorSupplier {
    PreProcessorFileDescriptorSupplier() {}
  }

  private static final class PreProcessorMethodDescriptorSupplier
      extends PreProcessorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PreProcessorMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PreProcessorGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PreProcessorFileDescriptorSupplier())
              .addMethod(getProtoPreprocessMethod())
              .build();
        }
      }
    }
    return result;
  }
}
