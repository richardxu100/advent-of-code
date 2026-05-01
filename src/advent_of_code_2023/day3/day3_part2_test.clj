(ns advent-of-code.day3.day3-part2-test
  (:require [clojure.test :refer :all]
            [advent-of-code.day3.part2 :as part2]))

(deftest it-returns-gear-ratio-product
  (is (= (part2/sum-of-gear-ratios [["3" "*" "."]
                                    ["4" "5" "."]
                                    ["." "." "5"]
                                    ["." "1" "5"]]) 135)))