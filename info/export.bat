REM       Exports the Javatools to the YAGO-NAGA Web page

SET SRC=..\src
SET SRCBIN=c:\fabian\eve\eve2\bin\javatools
SET DEST=%TEMP%\javatools

rmdir /S /Q %DEST%
mkdir %DEST%

for %%f in (src, bin, doc) do (
  mkdir %DEST%\%%f
)

javadoc -d %DEST%\doc -sourcepath %SRC% -subpackages javatools
xcopy /e %SRC%\*.* %DEST%\src
MKDIR %DEST%\bin\javatools
xcopy /e %SRCBIN%\*.* %DEST%\bin\javatools
cd /d %DEST%
call zip -r javatools.zip doc bin src

call NET USE y: \\data.mpi-sb.mpg.de\www\www.mpi-inf.mpg.de\yago-naga
copy /y %DEST%\javatools.zip y:\javatools
rmdir /S /Q y:\javatools\doc
mkdir y:\javatools\doc
xcopy /e %DEST%\doc\*.* y:\javatools\doc