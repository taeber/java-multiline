package com.rapczak.taeber.protobuf;

final class Tests {
  public static void main(String... args) throws Exception {
    Expression expr = buildAgentKnowsAgent(006, "James Bond");
    System.out.println(String.format("%s knows %s", expr.getSubject().getId(),
                                     expr.getObject().getName()));
    expr = buildLovesEveryone("You");
    System.out.println(String.format("%s love %s", expr.getSubject().getName(),
                                     expr.getObject().getName()));
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
  @ProtobufTemplate("AgentKnowsAgentTemplate")
  private static Expression buildAgentKnowsAgent(long agentId,
                                                 String fullname) {
    return AgentKnowsAgentTemplate.format(agentId, fullname);
  }

  /**
     subject: {
       name: "${name}"
     }
     predicate: LOVES
     object: {
       name: "Everyone!"
     }
   */
  @ProtobufTemplate("LovesEveryoneTemplate")
  private static Expression buildLovesEveryone(String name) {
    return LovesEveryoneTemplate.format(name);
  }
}
