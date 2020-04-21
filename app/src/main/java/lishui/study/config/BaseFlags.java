package lishui.study.config;

/**
 * Defines a set of flags used to control various launcher behaviors.
 *
 * All the flags should be defined here with appropriate default values. To override a value,
 * redefine it in {@link FeatureFlags}.
 *
 * This class is kept package-private to prevent direct access.
 */
public abstract class BaseFlags {

    BaseFlags() {}

    public static final boolean DEVELOPER_MODE = false;

    public static final boolean GRAY_SCREEN_ENABLED = false;

}
