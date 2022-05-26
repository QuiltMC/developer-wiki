# CONTRIBUTING

## General structure of the wiki

### Layout

```
/versions
    /version
        /section
            /tutorial
        /tutorial
    /version
        /section
            /tutorial
/libraries
    /library
        /tutorial
    /library
        /tutorial
```

* `versions`: A directory containing directories for each Minecraft version the wiki supports. This directory *does not*
  have a gradle project.
    * `version`: A directory containing directories for different sections. Only major versions should be targeted
      unless a minor version introduces a massive change, such as `1.18.2`. This directory *does* have a gradle project,
      but no code. The markdown file for this version should contain a summary for the code changes in the version,
      along with the most recent versions of Quilt projects, like QSL and Quilt Mappings, should be listed. These do not
      need to be constantly updated, but once the next version is out, they should be mostly stable.
        * `section`: A directory containing directories for different tutorials. A section provides an overview of a
          part of minecraft code, such as `items` or `networking`, these section can correspond to QSL modules, and that
          is a good place to start, but they do not have to. This project *does* contain a gradle project and can
          contain template or very basic code.
* `libraries`: A directory containing directories for some libraries minecraft uses, such as DFU, Brigadier, and GSON.
  This directory *does not* have a gradle project.
    * `library`: A directory containing directories for different tutorials for the library. This directory *does* have
      a gradle project and can contain template or very basic code. The markdown file for this library should include
      some basic information about the library, who made it, and what minecraft versions use each library
* `tutorial` (All instances above): A function gradle project that is well documented, both through its markdown file(s)
  and code.

Each directory with a gradle project should have a `markdown` directory, where markdown files are placed. Any images are
in `images`. Markdown files with different names than the tutorial are made into sub tutorials of the main tutorial for
the project.

### Markdown

The markdown parser we use is GitHub compatible, so write as if you were using GitHub.

We use a custom link resolver to make links easy to use when writing tutorials. Any link outside the wiki should start
with `https://` or `http://`. All internal links should be 100% relative so that when viewing the raw markdown files,
the links take you to the correct wiki. This is the same for images.

We have a custom feature that can directly place the contents of files into wiki pages. This uses similar syntax to normal code blocks for markdown:
~~~markdown
```file:path/to/file.ext
```
~~~
The wiki will automatically place the file contents into the code block. However, files are often too large, and so the wiki also supports adding regions from the code.
Regions are started with `//@start Region` and close with `//@end Region`, replacing `//` with the one line comment for the language file. Any code inside that is the placed into the code block. Overlapping regions strip the region comments.
A code block with the following syntax will target a region:
~~~markdown
```file:path/to/file.ext@Region
```
~~~

## Guide: New Tutorials

1. Check to make sure that no one else is working on the same or similar tutorial, and if so, help them.
2. Make sure you have forked this repository and cloned it locally.
3. Under the version you wish to add the tutorial for, either create a new section or add your tutorial under a current
   section.

## Conventions

See [QSL's conventions](https://github.com/QuiltMC/quilt-standard-libraries/blob/1.18/CONTRIBUTING.md#conventions)


