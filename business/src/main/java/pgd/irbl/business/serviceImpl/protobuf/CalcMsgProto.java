// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calc_msg.proto

package pgd.irbl.business.serviceImpl.protobuf;

public final class CalcMsgProto {
  private CalcMsgProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_serviceImpl_CalcRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_serviceImpl_CalcRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_serviceImpl_FileScore_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_serviceImpl_FileScore_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_serviceImpl_CalcReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_serviceImpl_CalcReply_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\016calc_msg.proto\022\013serviceImpl\"9\n\013CalcReq" +
      "uest\022\021\n\tfile_path\030\001 \001(\t\022\027\n\017bug_report_pa" +
      "th\030\002 \001(\t\"-\n\tFileScore\022\021\n\tfile_path\030\001 \001(\t" +
      "\022\r\n\005score\030\002 \001(\001\"H\n\tCalcReply\022\017\n\007succeed\030" +
      "\001 \001(\005\022*\n\nevaluation\030\002 \003(\0132\026.serviceImpl." +
      "FileScore2M\n\nCalculator\022?\n\tcalculate\022\030.s" +
      "erviceImpl.CalcRequest\032\026.serviceImpl.Cal" +
      "cReply\"\000B/\n\035pgd.irbl.business.serviceImp" +
      "lB\014CalcMsgProtoP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_serviceImpl_CalcRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_serviceImpl_CalcRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_serviceImpl_CalcRequest_descriptor,
        new String[] { "FilePath", "BugReportPath", });
    internal_static_serviceImpl_FileScore_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_serviceImpl_FileScore_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_serviceImpl_FileScore_descriptor,
        new String[] { "FilePath", "Score", });
    internal_static_serviceImpl_CalcReply_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_serviceImpl_CalcReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_serviceImpl_CalcReply_descriptor,
        new String[] { "Succeed", "Evaluation", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
