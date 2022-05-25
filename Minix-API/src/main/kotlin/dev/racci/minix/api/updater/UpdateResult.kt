package dev.racci.minix.api.updater

enum class UpdateResult(val isSuccessful: Boolean = false) {

    /**
     * The update was successful, and as readied it to be loaded the next time the server restarts/reloads.
     */
    SUCCESS(true),

    /**
     * The updater found an update.
     */
    UPDATE_FOUND(true),
    /**
     * The updater didn't find a new version.
     */
    NO_UPDATE,

    /**
     * The updater system has been disabled.
     */
    DISABLED,

    /**
     * The updater found an update, but was unable to download it.
     */
    FAILED_DOWNLOAD,

    /**
     * The updater found an update and was able to download it but failed extracting it.
     */
    FAILED_EXTRACTION,

    /**
     * The updater was unable to connect to the server to download the file.
     */
    FAILED_CONNECTION,

    /**
     * The updater could not find the current version.
     */
    FAILED_NO_VERSION,

    /**
     * The updater was unable to find a file.
     */
    FAILED_NO_FILE,

    /**
     * The updater had a miss configured API key or access token.
     */
    FAILED_KEY,

    /**
     * The updater found an update, but the provider doesn't allow us to download it.
     */
    FAILED_DOWNLOAD_DISALLOWED,

    /**
     * The updater found an update, but the version is not compatible with your system.
     */
    FAILED_VERSION_INCOMPATIBLE,

    /**
     * The updater was able to update the main plugin, but failed downloading the dependencies.
     */
    FAILED_DOWNLOAD_DEPENDENCIES,

    /**
     * The updater was unable to run because it has already been started.
     */
    FAILED_ALREADY_STARTED,

    /**
     * The updater was able to download the file however the checksum did not match.
     */
    FAILED_CHECKSUM,

    /**
     * The updater was unable to backup the plugin folder.
     */
    FAILED_BACKUP,

    /**
     * The updater was unable to get a local version.
     */
    FAILED_VERSION
}
