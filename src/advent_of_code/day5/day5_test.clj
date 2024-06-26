(ns advent-of-code.day5.day5-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day5.day5 :as day5]))

; parse which seeds need to be planted
; (destination range start) (source range start) (range length)

; so need to develop a mapping

(deftest it-returns-basic-target-mapping
  (is (= 25 (day5/calc-result [22] [[{:destination-start 23 :source-start 20 :range 50}]]))))

(deftest it-returns-basic-target-mapping-2
  (is (= 51 (day5/calc-result [22] [[{:destination-start 39 :source-start 10 :range 50}]]))))

(deftest it-returns-negative-value-mapping
  (is (= 16 (day5/calc-result [22] [[{:destination-start 4 :source-start 10 :range 50}]]))))

(deftest it-returns-seed-value-if-no-mapping
  (is (= 22 (day5/calc-result [22] [[{:destination-start 39 :source-start 10 :range 4}]]))))

(deftest it-returns-seed-value-if-no-mapping-2
  (is (= 22 (day5/calc-result [22] [[{:destination-start 15 :source-start 10 :range 4}]]))))

(deftest it-will-look-at-second-mapping-in-list
  (is (= 25 (day5/calc-result [22] [[{:destination-start 15 :source-start 10 :range 4}
                                    {:destination-start 20 :source-start 17 :range 30 }]]))))

(deftest it-converts-through-multiple-maps
  (is (= 25 (day5/calc-result [21] [[{:destination-start 21 :source-start 20 :range 50}]
                                    [{:destination-start 4 :source-start 1 :range 100}]]))))

(deftest it-converts-through-three-maps
  (is (= 30 (day5/calc-result [21] [[{:destination-start 21 :source-start 20 :range 50}]
                                    [{:destination-start 4 :source-start 1 :range 100}]
                                    [{:destination-start 15 :source-start 10 :range 100}]
                                    ]))))

(deftest it-returns-seed-through-multiple-maps
  (is (= 1000 (day5/calc-result [1000] [[{:destination-start 21 :source-start 20 :range 50}]
                                    [{:destination-start 4 :source-start 1 :range 100}]
                                    [{:destination-start 15 :source-start 10 :range 100}]
                                    ]))))

(deftest it-returns-lowest-location-number-for-any-seed
  (is (= 5 (day5/calc-result [100 3] [[{:destination-start 2 :source-start 0 :range 100}]]))))