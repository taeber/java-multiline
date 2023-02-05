package com.rapczak.taeber.protobuf;

import com.google.protobuf.TextFormat;

/**
   subject: {
     id: %d
   }
   predicate: KNOWS
   object: {
     name: "%s"
   }
 */
@Templated
interface AgentKnowsAgent {
  Expression format(long agentId, String fullname);
}

public final class Tests {

  public static void main(String... args) throws Exception {
    // "Inject"
    // TemplateagentKnowsAgentTemplate.init();
    var tests = new Tests();
    tests.run();
  }

  /**
     subject: {
       id: %d
     }
     predicate: KNOWS
     object: {
       name: "%s"
     }
   */
  // @Templated static ProtobufTemplate<Expression> agentKnowsAgentTemplate;

  public void run() throws Exception {
    // var expr = agentKnowsAgentTemplate.format(9, "Eight");
    // System.out.println(String.format("%s knows %s",
    // expr.getSubject().getId(),
    //                                  expr.getObject().getName()));

    Expression expr = AgentKnowsAgentTemplate.format(006, "James Bond");
    System.out.println(String.format("%s knows %s", expr.getSubject().getId(),
                                     expr.getObject().getName()));
    expr = buildAgentKnowsAgent(8, "Everyone");
    System.out.println(String.format("%s knows %s", expr.getSubject().getId(),
                                     expr.getObject().getName()));
  }

  public void OLDrun() throws Exception {
    System.out.println("This is only a test");
    var expr = Expression.newBuilder()
                   .setSubject(Thing.newBuilder().setId(006))
                   .setPredicate(Predicate.KNOWS)
                   .setObject(Thing.newBuilder().setName("James Bond"))
                   .build();
    System.out.println(String.format("%s knows %s", expr.getSubject().getId(),
                                     expr.getObject().getName()));

    var msg = String.format(""
                                + "subject: {                 "
                                + "  id: %d                   "
                                + "}                          "
                                + "predicate: KNOWS "
                                + "object: {                  "
                                + "  name: \"%s\"             "
                                + "}                          ",
                            007, "Alec ??");

    var builder = Expression.newBuilder();
    TextFormat.getParser().merge(msg, builder);
    expr = builder.build();
    System.out.println(String.format("%s knows %s", expr.getSubject().getId(),
                                     expr.getObject().getName()));

    // expr = buildAgentKnowsAgent(8, "Everyone");
    // System.out.println(String.format("%s knows %s",
    // expr.getSubject().getId(),
    //                                  expr.getObject().getName()));
  }

  /**
     subject: {
       id: ${agentId}
     }
     predicate: KNOWS
     object: {
       name: "${fullname}"
     }
   */
  @Templated
  private static Expression buildAgentKnowsAgent(long agentId,
                                                 String fullname) {
    // return buildAgentKnowsAgentTemplate.format(agentId, fullname);
    return AgentKnowsAgentTemplate.format(agentId, fullname);
  }

  // static Expression buildAgentKnowsAgent2(long agentId, String fullname) {
  //   /**
  //      subject: {
  //        id: ${agentId}
  //      }
  //      predicate: KNOWS
  //      object: {
  //        name: "${fullname}"
  //      }
  //    */
  //   @Templated
  //   Expression expr = AgentKnowsAgentTemplate.format(agentId, fullname);
  //   // Expression expr = buildAgentKnowsAgentTemplate.format(agentId,
  //   fullname); return expr;
  // }
}

// AgentKnowsAgentTemplate.format()

//     final class AgentKnowsAgentTemplate {
//   static Expression format(long agentId, String fullname) {
//     return Expression.newBuilder()
//         .setSubject(Thing.newBuilder().setId(agentId))
//         .setPredicate(Predicate.KNOWS)
//         .setObject(Thing.newBuilder().setName(fullname))
//         .build();
//   }
//   private AgentKnowsAgentTemplate() {}
// }

// final class generateclass implements Protobuf<Expression> {
//   static void inject() {
//     System.out.println("Hello from generateclass static init");
//     Tests.agentKnowsAgentTemplate = new generateclass();
//   }

//   private static String msg = ""
//                               + "subject: {                 "
//                               + "  id: %d                   "
//                               + "}                          "
//                               + "predicate: KNOWS "
//                               + "object: {                  "
//                               + "  name: \"%s\"             "
//                               + "}                          ";

//   @Override
//   public <T1, T2> Expression format(T1 arg1, T2 arg2) {
//     var builder = Expression.newBuilder();
//     try {
//       com.google.protobuf.TextFormat.getParser().merge(
//           String.format(msg, arg1, arg2), builder);
//     } catch (Exception ex) {
//       throw new RuntimeException(ex);
//     }
//     return builder.build();
//   }
// }