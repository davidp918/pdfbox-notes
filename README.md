## Java pdfbox notes

Notes of Java pdfbox with comments and interpretations, involving pdf manipulations such as creation, text formation, page removal, file/attachment insertion, content extraction, metadata operation, and more.

## Folder Structure

The workspace contains two main folders, where:

- `src`: the folder to maintain source code
- `pdfs`: the folder to save pdf files

## Break down

* #### Initialization
  * `create` - create a pdf file
* #### Page
  * `add` - add a number of pages
  * `remove` - remove a desired page
  * `split` - split pages of a pdf into seperated files
  * `merge` - merge multiple pdfs into one
* #### Text
  * `addOneLine` - add a single line text
  * `addMultipleLine` - add texts of multiple lines
  * `extract` - extract all texts from a pdf
  * `extractPassport` - example of extracting desired information using regex
* #### Attachment
  * `add` - add an attachment to the pdf
  * `get` - retrieve all the attachments of the pdf
* #### MetaData
  * `set` - set metadata
  * `get` - get metadata
* #### Image
  * `insert` - insert an image to a specific page
  * `extract` - extract the images of a specific page
  * `getLocationsAndSize` - get the locations and sizes of all the images present in the pdf   