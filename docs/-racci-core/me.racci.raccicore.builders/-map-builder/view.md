//[RacciCore](../../../index.md)/[me.racci.raccicore.builders](../index.md)/[MapBuilder](index.md)/[view](view.md)

# view

[jvm]\
fun [view](view.md)(view: MapView): [MapBuilder](index.md)

Sets the associated map. This is used to determine what map is displayed.

The implementation **may** allow null to clear the associated map, but this is not required and is liable to generate a new (undefined) map when the item is first used.

#### Return

[MapBuilder](index.md)

#### Since

0.1.5

## Parameters

jvm

| | |
|---|---|
| view | the map to set |

[jvm]\
var [view](view.md): MapView?
