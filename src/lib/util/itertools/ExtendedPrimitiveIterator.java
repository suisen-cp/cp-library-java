package lib.util.itertools;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

import lib.util.function.ByteConsumer;
import lib.util.function.CharConsumer;

public interface ExtendedPrimitiveIterator<T, T_CONS> extends PrimitiveIterator<T, T_CONS> {
    interface OfChar extends PrimitiveIterator<Character, CharConsumer> {
        char nextChar();
        default void forEachRemaining(CharConsumer action) {
            Objects.requireNonNull(action);
            while (hasNext())
                action.accept(nextChar());
        }
        @Override
        default Character next() {
            return nextChar();
        }
        @Override
        default void forEachRemaining(Consumer<? super Character> action) {
            if (action instanceof CharConsumer) {
                forEachRemaining((CharConsumer) action);
            }
            else {
                Objects.requireNonNull(action);
                forEachRemaining((CharConsumer) action::accept);
            }
        }
    }
    interface OfByte extends PrimitiveIterator<Byte, ByteConsumer> {
        byte nextByte();
        default void forEachRemaining(ByteConsumer action) {
            Objects.requireNonNull(action);
            while (hasNext())
                action.accept(nextByte());
        }
        @Override
        default Byte next() {
            return nextByte();
        }
        @Override
        default void forEachRemaining(Consumer<? super Byte> action) {
            if (action instanceof ByteConsumer) {
                forEachRemaining((ByteConsumer) action);
            }
            else {
                Objects.requireNonNull(action);
                forEachRemaining((ByteConsumer) action::accept);
            }
        }
    }
}