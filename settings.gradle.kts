rootProject.name = "ruler-root"

include(
    ":library:ruler",
    ":library:ruler-android",
    ":library:ruler-android-startup",
    ":sample",
)

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
  defaultLibrariesExtensionName.set("deps")
}
