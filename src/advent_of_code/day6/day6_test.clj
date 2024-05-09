(ns advent-of-code.day6.day6-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day6.day6 :as day6]))

(deftest it-returns-for-simple-single-race
  (is (= (day6/margin-of-error [{:time 7 :record 9}]) 4)))


(deftest it-returns-for-simple-single-race-2
  (is (= (day6/margin-of-error [{:time 15 :record 40}]) 8)))


(deftest it-returns-0-for-impossible-record
  (is (= (day6/margin-of-error [{:time 15 :record 400}]) 0)))

(deftest it-returns-product-for-multiple-races
  (is (= (day6/margin-of-error [{:time 7 :record 9} {:time 15 :record 40}]) 32)))

(deftest it-returns-product-for-3-races
  (is (= (day6/margin-of-error [{:time 7 :record 9} {:time 15 :record 40} {:time 30 :record 200}]) 288)))