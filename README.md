This is a Java Web application built upon Java 7, J2EE 6 and plain Servlet/JSP on server side, respectively JQuery 3, Bootstrap 4 and some custom JavaScript in the browser. It is designed to work in a Servlet 3.1 container - Tomcat 8 was used for development.

The scope is to introduce internationalization (i18n) and display messages in a Web browser. The code builds on the [WebPaginationPOC](https://github.com/olddimplex/WebPaginationPOC), thus implementing all the concepts presented there.

#### Internationalization

Translation data is kept in an in-memory map with the phrase in English and language code as a key. It is loaded from a classpath resourse (file) of a CSV or XLSX format. CSV format is easy to create, but suffers from:

- Need to use a delimiter, which means it cannot appear in the string values. This can be solved by enclosing the values in quotes, which in turn introduces a quote character. If the quote character is to appear in a string value, it can be doubled. However, it is impossible to detect a wrongly typed string value, because there is no way to know if the intention was it to be quoted or not;
- Difficulties when a number of people are to work on the same translation document. The document itself is prepared by the developer and then provided for translation. This process may include multiple iterations and there may be a need to comment/agree on certain phrases, especially when there are placeholders in them;
- Character encoding is not specified in the file and must be assumed.

Maintaining translations in XLSX format resolves the above issues.

As a rule, data coming from backend is not subject to translation in the presentation layer. If such data is to be internationalized, the backend is expected to do this by itself.

#### Display messages

Displaying messages may not be so straightforward because of the streamed page content - the message placeholder already gone when the need to show it appears. In such situations, a DisplayMessage object is stored in user's HTTP session and a hidden \<input\> tag is generated with a distinguished name and value specifying the marker class of the area(s) to be updated. The JavaScript code on client side makes a secondary AJAX request and populates the respective window areas. The server code responds to these requests and then clears the message object(s) from HTTP session.

