(ns joy-of-clojure.functional)

([:a :b] 0)

(map [:a :b :c :d] #{0 2})
(map #{0 2} [:a :b :c :d])                                 ; not the same

(def fifth
  "docstring"
  (comp first rest rest rest rest))                         ; can't use defn here with compose

(fifth [1 2 3 4 5 6])

(defn fnth
  "docstring"
  [n]
  (apply comp
         (cons first (take (dec n) (repeat rest)))))

((fnth 6) '[1 2 3 4 5 6 7])

(def lowercase-item (comp
                       keyword
                       #(.toLowerCase %)
                       name))
(map lowercase-item
     '(a B C))