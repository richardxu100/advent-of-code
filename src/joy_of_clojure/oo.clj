(ns joy-of-clojure.oo
  (:import (java.util List)))

(defprotocol Concatenable
  (my-cat [this other]))

(extend-type String
  Concatenable
  (my-cat [this other]
    (.concat this other)))

(my-cat "House" " of Leaves")

(extend-type List
  Concatenable
  (my-cat [this other]
    (concat this other)))

(my-cat [1 2 3] [4 5 6])