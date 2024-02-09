(ns exercises.bowling-game
  (:require [clojure.test :refer :all]))

(defn to-frames [rolls]
  (loop [remaining-rolls
         rolls
         frames []]
    (cond (empty? remaining-rolls)
          frames
          (= 10 (first remaining-rolls))
          (recur (rest remaining-rolls) (conj frames (take 3 remaining-rolls)))
          (= 10 (reduce + (take 2 remaining-rolls)))
          (recur (drop 2 remaining-rolls) (conj frames (take 3 remaining-rolls)))
          :else (recur (drop 2 remaining-rolls) (conj frames (take 2 remaining-rolls))))))

(defn add-frame
  "docstring"
  [score frame]
  (+ score (reduce + frame)))

(defn score
  "docstring"
  [rolls]
  (reduce add-frame 0 (to-frames rolls)))

(deftest for-0
  (is (= 0 (score (repeat 20 0)))))

(deftest for-20
  (is (= 20 (score (repeat 20 1)))))

(deftest for-24
  (is (= 24 (score (concat [5 5 7] (repeat 17 0))))))

(deftest for-strike
  (is (= 20 (score (concat [10 2 3] (repeat 1))))))