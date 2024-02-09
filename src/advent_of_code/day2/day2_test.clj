(ns advent-of-code.day2.day2-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day2.day2 :as day2]))

(deftest it-returns-for-valid-game-1
  (is (= 1 (day2/calculate-sum-of-ids ["Game 1: 1 blue"]))))

(deftest it-returns-zero-for-invalid-game-1
  (is (= 0 (day2/calculate-sum-of-ids ["Game 1: 100 blue"]))))

(deftest it-returns-zero-for-invalid-game-1-from-green-value
  (is (= 0 (day2/calculate-sum-of-ids ["Game 1: 1 blue 14 green"]))))

(deftest it-returns-zero-for-invalid-game-1-from-red-value
  (is (= 0 (day2/calculate-sum-of-ids ["Game 1: 1 blue 1 green 13 red"]))))

(deftest it-handles-without-blue-value
  (is (= 1 (day2/calculate-sum-of-ids ["Game 1: 1 green 1 red"]))))

(deftest it-returns-zero-when-second-round-is-not-valid
  (is (= 0 (day2/calculate-sum-of-ids ["Game 1: 1 blue 2 red; 100 red"]))))
