This is a Java Web application built upon Java 7, J2EE 6 and plain Servlet/JSP on server side, respectively JQuery 3, Bootstrap 4 and some custom JavaScript in the browser. It is designed to work in a Servlet 3.1 container - Tomcat 8 was used for development.

The scope is to introduce internationalization (i18n) and display messages in a Web browser. The code builds on the [WebPaginationPOC](https://github.com/olddimplex/WebPaginationPOC), thus implementing all the concepts presented there.

Translation data is kept in an in-memory map with the phrase in English and language code as a key. It is loaded from a classpath resourse (file) of a CSV or XLSX format.

Displaying messages may not be so simple because of the streamed page content. The message placeholder may be already gone when the need of it is realized.

