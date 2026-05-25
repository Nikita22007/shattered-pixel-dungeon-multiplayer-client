package com.nikita22007.multiplayer.utils.text;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LocalizedString {

    public enum Mode {
        KEY,
        RAW,
        TRANSFORM,
        CONCAT
    }

    public enum Transform {
        CAPITALIZE,
        TITLE_CASE,
        UPPER_CASE,
        LOWER_CASE
    }

    private final Mode mode;
    private final LocalizedKey key;
    private final String raw;
    private final Object[] args;
    private final Transform transform;
    private final LocalizedString text;
    private final Object[] parts;

    private LocalizedString(Mode mode, LocalizedKey key, String raw, Object[] args, Transform transform, LocalizedString text, Object[] parts) {
        this.mode = mode;
        this.key = key;
        this.raw = raw;
        this.args = args == null ? new Object[0] : args;
        this.transform = transform;
        this.text = text;
        this.parts = parts == null ? new Object[0] : parts;
    }

    public static LocalizedString key(LocalizedKey key, Object... args) {
        return new LocalizedString(Mode.KEY, key, null, args, null, null, null);
    }

    public static LocalizedString raw(String raw, Object... args) {
        return new LocalizedString(Mode.RAW, null, raw, args, null, null, null);
    }

    public static LocalizedString transform(Transform transform, LocalizedString text) {
        return new LocalizedString(Mode.TRANSFORM, null, null, null, transform, text, null);
    }

    public static LocalizedString concat(Object... parts) {
        ArrayList<Object> flattened = new ArrayList<>();
        flattenConcatParts(flattened, parts);
        return new LocalizedString(Mode.CONCAT, null, null, null, null, null, flattened.toArray(new Object[0]));
    }

    private static void flattenConcatParts(ArrayList<Object> flattened, Object[] parts) {
        if (parts == null) {
            return;
        }
        for (Object part : parts) {
            if (part instanceof LocalizedString && ((LocalizedString) part).mode() == Mode.CONCAT) {
                flattenConcatParts(flattened, ((LocalizedString) part).parts());
            } else {
                flattened.add(part);
            }
        }
    }

    public Mode mode() {
        return mode;
    }

    public LocalizedKey key() {
        return key;
    }

    public String raw() {
        return raw;
    }

    public Object[] args() {
        return args;
    }

    public Transform transform() {
        return transform;
    }

    public LocalizedString text() {
        return text;
    }

    public Object[] parts() {
        return parts;
    }

    @Override
    public String toString() {
        return Messages.resolve(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocalizedString)) {
            return false;
        }
        LocalizedString other = (LocalizedString) obj;
        return mode == other.mode
                && Objects.equals(key, other.key)
                && Objects.equals(raw, other.raw)
                && Arrays.equals(args, other.args)
                && transform == other.transform
                && Objects.equals(text, other.text)
                && Arrays.equals(parts, other.parts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mode, key, raw, transform, text);
        result = 31 * result + Arrays.hashCode(args);
        result = 31 * result + Arrays.hashCode(parts);
        return result;
    }
}
