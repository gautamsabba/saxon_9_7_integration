<results>
  {
    let $a := doc("bib.xml")//author
    let $b:= position($a)
    for $last in distinct-values($a/last),
        $first in distinct-values($a[last=$last]/first)
    order by $last, $first
    return
        <result>
        </result>
  }
</results> 