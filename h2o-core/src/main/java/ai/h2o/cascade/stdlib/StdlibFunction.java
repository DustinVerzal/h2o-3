package ai.h2o.cascade.stdlib;

import ai.h2o.cascade.core.Function;
import ai.h2o.cascade.vals.Val;

import static ai.h2o.cascade.vals.Val.Type.*;


/**
 * Base class for all functions in the Cascade Standard Library.
 */
public abstract class StdlibFunction extends Function {

  /**
   * Stub method: actual implementation will be generated by the weaver
   * in {@link ai.h2o.cascade.CascadeStandardLibrary}.
   */
  @Override
  public Val apply0(Val[] args) {
    throw new RuntimeException("Missing generated method apply0(Val[]) for " + getClass().getName() + ".\n" +
                               "Did you forget to load CascadeStandardLibrary class?");
  }


  //--------------------------------------------------------------------------------------------------------------------
  // Exceptions
  //--------------------------------------------------------------------------------------------------------------------

  /**
   * Exception to be raised within the body of an stdlib function, indicating
   * that the type of the {@code index}'th argument is incorrect.
   *
   * <p>If the type error refers to overall shape of all arguments, then
   * {@code index} will be -1.
   *
   * <p>This exception will be caught within {@code AstApply} and converted
   * into a {@link ai.h2o.cascade.Cascade.TypeError}, translating the
   * argument's {@code index} into its location within the cascade expression
   * being executed.
   */
  public class TypeError extends IllegalArgumentException {
    public int index;

    public TypeError(int i, String message) {
      super(message);
      index = i;
    }
  }


  /**
   * Exception to be raised within the body of an stdlib function, indicating
   * that the value of the {@code i}'th argument is incorrect.
   *
   * <p>This exception will be caught within {@code AstApply} and converted
   * into a {@link ai.h2o.cascade.Cascade.ValueError}, translating the
   * argument's {@code index} into its location within the cascade expression
   * being executed.
   */
  public class ValueError extends IllegalArgumentException {
    public int index;

    public ValueError(int i, String message) {
      super(message);
      index = i;
    }
  }


  /**
   * Exception to be raised within the body of an stdlib function, indicating
   * an error condition that is not related to any argument in particular.
   *
   * <p>This exception will be caught within {@code AstApply} and converted
   * into a {@link ai.h2o.cascade.Cascade.RuntimeError}.
   */
  public class RuntimeError extends RuntimeException {
    public RuntimeError(String message) {
      super(message);
    }
  }



  //--------------------------------------------------------------------------------------------------------------------
  // Helpers for apply0()
  //--------------------------------------------------------------------------------------------------------------------

  /**
   * Perform verification that the number of arguments supplied to a function
   * is within the expected bounds, or throw an exception otherwise.
   *
   * @param actualN Number of arguments passed to a function
   * @param expectedMin Smallest acceptable count of arguments
   * @param expectedMax Largest acceptable count of arguments. It is often the
   *                    case that {@code expectedMin == expectedMax}.
   */
  @SuppressWarnings("unused")  // Used in the apply0() method generated by the CascadeStandardLibrary
  protected final void argumentsCountCheck(int actualN, int expectedMin, int expectedMax) {
    if (expectedMin == expectedMax) {
      if (actualN != expectedMin) {
        String count = expectedMin + " argument" + (expectedMin == 1? "" : "s");
        String actual = actualN + " argument" + (actualN == 1? "" : "s");
        throw new TypeError(-1, "Expected " + count + " but got " + actual);
      }
    } else {
      if (actualN < expectedMin) {
        String count = expectedMin + " argument" + (expectedMin == 1? "" : "s");
        String actual = actualN + " argument" + (actualN == 1? "" : "s");
        throw new TypeError(-1, "Expected at least " + count + " but got " + actual);
      }
      if (actualN > expectedMax && expectedMax != -1) {
        String count = expectedMax + " argument" + (expectedMax == 1? "" : "s");
        String actual = actualN + " argument" + (actualN == 1? "" : "s");
        throw new TypeError(-1, "Expected at most " + count + " but got " + actual);
      }
    }
  }


  /**
   * Verify that argument {@code i} within the {@code args} has the expected
   * type, or throw an error otherwise.
   *
   * @param args Array of all arguments to a function.
   * @param i Index of the element to test.
   * @param expType Expected type for element {@code args[i]}.
   */
  @SuppressWarnings("unused")  // Used in the apply0() method generated by the CascadeStandardLibrary
  protected final void checkArg(Val[] args, int i, Val.Type expType) {
    Val.Type actualType = args[i].type();
    if (actualType == expType || actualType == NULL ||
        actualType == NUMS && (expType == INT && args[i].maybeInt() ||
                               expType == BOOL && args[i].maybeBool()))
      return;
    throw new TypeError(i, "Expected argument of type " + expType + " but instead got " + actualType);
  }
}
