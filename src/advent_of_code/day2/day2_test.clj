(ns advent-of-code.day2.day2-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day2.day2 :as day2]))

(deftest it-returns-for-valid-game-1
  (is (= 1 (day2/calculate-sum-of-ids ["Game 1: 1 blue"]))))

(deftest it-returns-for-valid-game-2
  (is (= 2 (day2/calculate-sum-of-ids ["Game 1: 100 blue" "Game 2: 1 blue"]))))
