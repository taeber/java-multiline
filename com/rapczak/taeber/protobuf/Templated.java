package com.rapczak.taeber.protobuf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Templated {
  String value() default "";
}

interface ProtobufTemplate<R> {
  default R format() { throw new UnsupportedOperationException(); }
  default<T1> R format(T1 arg1) { throw new UnsupportedOperationException(); }
  default<T1, T2> R format(T1 arg1, T2 arg2) {
    throw new UnsupportedOperationException();
  }
  // ...
}
