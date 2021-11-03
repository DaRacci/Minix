//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.items.builders](../index.md)/[BookBuilder](index.md)/[page](page.md)

# page

[jvm]\
fun [page](page.md)(vararg pages: Component): [BookBuilder](index.md)

Adds new pages to the end of the book. Up to a maximum of 50 pages with 256 characters per page.

#### Return

[BookBuilder](index.md)

#### Since

3.0.1

## Parameters

jvm

| | |
|---|---|
| pages | list of pages |

[jvm]\
fun [page](page.md)(page: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), data: Component): [BookBuilder](index.md)

Sets the specified page in the book. Pages of the book must be contiguous.

The data can be up to 256 characters in length, additional characters are truncated.

Pages are 1-indexed.

#### Return

[BookBuilder](index.md)

#### Since

3.0.1

## Parameters

jvm

| | |
|---|---|
| page | the page number to set, in range [1, BookMeta.getPageCount] |
| data | the data to set for that page |
