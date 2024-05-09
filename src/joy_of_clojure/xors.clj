(ns joy-of-clojure.xors)

(defn xors
  "docstring"
  [max-x max-y]
  (for [x (range max-x) y (range max-y)] [x y (rem (bit-xor x y) 256)]))