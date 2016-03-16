xquery version "1.0" encoding "UTF-8";

declare namespace test="http://www.oxygenxml.com/test";

declare variable $TEST := 2;

declare function test:log()
{
  <a/>
};


<demo>{  
    test:log() 
}</demo>



