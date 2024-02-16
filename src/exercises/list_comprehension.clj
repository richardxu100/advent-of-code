(ns exercises.list-comprehension
  (:require [clojure.string :as string]))

(for [number [1 2 3]]
  (* number 2))

(for [number [1 2 3]
      letter [:a :b :c]]
  (str number letter))

(for [tumbler-1 (range 10)
      tumbler-2 (range 10)
      tumbler-3 (range 10)
      :when (or (= tumbler-1 4)
                (= tumbler-2 4)
                (= tumbler-3 4))]
  [tumbler-1 tumbler-2 tumbler-3])

(for [tumbler-1 (range 10)
      tumbler-2 (range 10)
      tumbler-3 (range 10)
      :when (or (= tumbler-1 tumbler-2)
                (= tumbler-2 tumbler-3)
                (= tumbler-3 tumbler-1))]
  [tumbler-1 tumbler-2 tumbler-3])

(def blacklisted #{\I \O})

(def capital-letters (map char (range (int \A) (inc (int \Z)))))



(for [letter-1 capital-letters
      letter-2 capital-letters
      :when (and (not (blacklisted letter-1))
                 (not (blacklisted letter-2)))
      ]
  (str letter-1 letter-2)
  )

(for [number [1 2 3]
      :let [tripled (* number 3)]
      :while (odd? tripled)]
  tripled)

(defn is-palindrome?
  "docstring"
  [string]
  (= (string/reverse string) string))

(is-palindrome? "101")

(apply max (for [number (range 100 1000)
                 another-number (range 100 1000)
                 :let [product (* number another-number)]
                 :when (is-palindrome? (str product))
                 ]
             product))