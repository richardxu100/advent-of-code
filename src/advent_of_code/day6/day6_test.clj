(ns advent-of-code.day6.day6-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day6.day6 :as day6]))

(deftest it-returns-for-simple-single-race
  (is (= (day6/margin-of-error [{:time 7 :record 9}]) 4)))
