(ns advent-of-code.day4.day4
  (:require [clojure.string :as str]))

(defn calc-points [card]
  (let [[_ remaining] (str/split card #":")
        [winning-nums-string my-nums-string] (str/split remaining #"\|")
        winning-nums (set (map parse-long (filter seq (str/split winning-nums-string #" "))))
        my-nums (map parse-long (filter seq (str/split my-nums-string #" ")))
        num-matches (count (filter true? (map (fn [my-num] (contains? winning-nums my-num)) my-nums)))]
    (println num-matches)
    (if (zero? num-matches)
      0
      (Math/pow 2 (dec num-matches)))
    ))

(let [cards (->
              "./src/advent_of_code/day4/cards.txt"
              slurp
              (str/split #"\n")
              )]
  (->>
    cards
    (map calc-points)
    (apply +)))

