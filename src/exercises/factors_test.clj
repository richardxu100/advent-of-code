(ns exercises.factors-test
  (:require [clojure.test :refer :all]))

(defn old-prime-factors-of [n]
  (if (> n 1)
    (if (zero? (rem n 2))
      (cons 2 (old-prime-factors-of (quot n 2)))
      [n])
    []))

(defn better-prime-factors-of [n]
  (if (> n 1)
    (if (zero? (rem n 2))
      (cons 2 (better-prime-factors-of (quot n 2)))
      (if (zero? (rem n 3))
        (cons 3 (better-prime-factors-of (quot n 3)))
        [n]))
    []))

(defn prime-factors-of
  "docstring"
  [n]
  (loop [n n
         divisor 2
         factors []]
    (if (> n 1)
      (if (zero? (rem n divisor))
        (recur (quot n divisor) divisor (conj factors divisor))
        (recur n (inc divisor) factors))
      factors)))

(deftest for-1
  (is (= [] (prime-factors-of 1))))

(deftest for-2
  (is (= [2] (prime-factors-of 2))))

(deftest for-3
  (is (= [3] (prime-factors-of 3))))

(deftest for-4
  (is (= [2 2] (prime-factors-of 4))))

(deftest for-5
  (is (= [5] (prime-factors-of 5))))

(deftest for-6
  (is (= [2 3] (prime-factors-of 6))))

(deftest for-7
  (is (= [7] (prime-factors-of 7))))

(deftest for-8
  (is (= [2 2 2] (prime-factors-of 8))))

(deftest for-9
  (is (= [3 3] (prime-factors-of 9))))

(deftest for-17
  (is (= [17] (prime-factors-of 17))))

(deftest for-34
  (is (= [2 17] (prime-factors-of 34))))

