package dev.racci.minix.api.updater

enum class ReleaseType {
    /**
     * An Alpha build.
     */
    ALPHA,

    /**
     * A Beta build.
     */
    BETA,

    /**
     * A Release build.
     */
    RELEASE,

    /**
     * A Snapshot build.
     */
    SNAPSHOT,

    /**
     * A Release Candidate build.
     */
    RC,

    /**
     * An unknown build.
     */
    UNKNOWN
}
