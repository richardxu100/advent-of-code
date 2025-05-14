There is a concept of stable sorting, which means that elements that are treated as equal by the comparator, will appear in the order that occurs in the list.

So there won't be a non-deterministic output from running the same sorting algorithm multiple times.

Clojure's sort is stable