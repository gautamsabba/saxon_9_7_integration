@echo off

SET CP=lib/saxon9ee.jar
SET CP=%CP%;lib/saxon-license.lic

java -Xmx60m -cp %CP% net.sf.saxon.Transform %*