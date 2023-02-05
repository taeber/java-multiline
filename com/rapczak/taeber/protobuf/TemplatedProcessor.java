package com.rapczak.taeber.protobuf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("com.rapczak.taeber.protobuf.Templated")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public final class TemplatedProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment env) {
    var logger = processingEnv.getMessager();
    try {
      var annotated = env.getElementsAnnotatedWith(Templated.class);
      for (var element : annotated) {
        var pkg = processingEnv.getElementUtils().getPackageOf(element);
        var name = element.getSimpleName();
        // TODO: getEnclosingElements() -> look for format(). Get Return and
        // Params
        logger.printMessage(Kind.NOTE, TemplatedProcessor.class.getName() +
                                           " is processing" + name);
        var outfile = processingEnv.getFiler().createSourceFile(String.format(
            "%s.%sTemplate", pkg.getQualifiedName().toString(), name));
        var comment = processingEnv.getElementUtils()
                          .getDocComment(element)
                          .trim()
                          .replace("\"", "\\\"")
                          .replace("\n", "\\n");
        logger.printMessage(Kind.NOTE, comment, element);
        if (element.getKind() == ElementKind.LOCAL_VARIABLE) {
          continue;
        }
        var result = "Expression";
        var params = "long agentId, String fullname";
        try (var out = new PrintWriter(outfile.openWriter())) {
          out.println(String.format("package %s;", pkg));
          // TODO: maybe implement interface and have singleton
          out.println(String.format("final class %sTemplate {", name));
          out.println(
              String.format("  private static String msg = \"%s\"; ", comment));
          out.println(
              String.format("  public static %s format(%s) {", result, params));
          out.println(
              String.format("    var builder = %s.newBuilder();", result));
          out.println("    try {");
          out.println(
              "      com.google.protobuf.TextFormat.getParser().merge(");
          out.println(
              String.format("          String.format(msg, %s), builder);",
                            "agentId, fullname"));
          out.println("    } catch (Exception ex) {");
          out.println("      throw new RuntimeException(ex);");
          out.println("    }");
          out.println("    return builder.build();");
          out.println("  }");
          out.println("}");
        }
      }
      return true;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}

// @SupportedAnnotationTypes("com.rapczak.taeber.protobuf.Templated")
// @SupportedSourceVersion(SourceVersion.RELEASE_11)
// final class TemplatedProcessor extends AbstractProcessor {

//   @Override
//   public boolean process(Set<? extends TypeElement> annotations,
//                          RoundEnvironment env) {
//     var logger = processingEnv.getMessager();
//     try {
//       logger.printMessage(Kind.NOTE, TemplatedProcessor.class.getName() +
//                                          " is processing.");
//       var annotated = env.getElementsAnnotatedWith(Templated.class);
//       for (var element : annotated) {
//         if (!element.getModifiers().contains(Modifier.STATIC)) {
//           logger.printMessage(Kind.ERROR, "must be static.", element);
//           return false;
//         }
//         if (element.getModifiers().contains(Modifier.PRIVATE)) {
//           logger.printMessage(Kind.ERROR, "cannot be private.", element);
//           return false;
//         }
//         // var outfile =
//         //
//         processingEnv.getFiler().createSourceFile(element.getSimpleName());
//         // try (var out = new PrintWriter(outfile.openWriter())) {
//         //   out.println("interface Taeber {} /*");
//         //   var comment =
//         //   processingEnv.getElementUtils().getDocComment(element);
//         //   out.println(element.getSimpleName());
//         //   out.println(comment);
//         //   out.println("*/");
//         // }
//         var pkg = processingEnv.getElementUtils().getPackageOf(element);
//         var name = element.getSimpleName();
//         var outfile =
//         processingEnv.getFiler().createSourceFile(String.format(
//             "%s.Template%s", pkg.getQualifiedName().toString(), name));
//         var comment = processingEnv.getElementUtils()
//                           .getDocComment(element)
//                           .trim()
//                           .replace("\"", "\\\"")
//                           .replace("\n", "\\n");
//         try (var out = new PrintWriter(outfile.openWriter())) {
//           out.println(String.format("package %s;", pkg));
//           out.println(String.format(
//               "final class Template%s implements ProtobufTemplate<Expression>
//               {", name));
//           out.println("  static void init (){");
//           out.println(String.format(
//               "    %s.%s = new Template%s();",
//               element.getEnclosingElement().getSimpleName(), name, name));
//           out.println("  }");
//           out.println(
//               String.format("  private static String msg = \"%s\"; ",
//               comment));
//           out.println("  @Override");
//           out.println(
//               "  public <T1, T2> Expression format(T1 arg1, T2 arg2) {");
//           out.println("    var builder = Expression.newBuilder();");
//           out.println("    try {");
//           out.println(
//               "      com.google.protobuf.TextFormat.getParser().merge(");
//           out.println("          String.format(msg, arg1, arg2), builder);");
//           out.println("    } catch (Exception ex) {");
//           out.println("      throw new RuntimeException(ex);");
//           out.println("    }");
//           out.println("    return builder.build();");
//           out.println("  }");
//           out.println("}");
//         }
//       }
//       return true;
//     } catch (IOException ex) {
//       throw new RuntimeException(ex);
//     }
//   }
// }