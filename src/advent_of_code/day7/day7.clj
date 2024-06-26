(ns advent-of-code.day7.day7
  (:require [clojure.string :as str]))

(def card-ranking-map
  {\2 2
   \3 3
   \4 4
   \5 5
   \6 6
   \7 7
   \8 8
   \9 9
   \T 10
   \J 11
   \Q 12
   \K 13
   \A 14})

(card-ranking-map "A")

(def hand-ranking-map
  {
   :high-card       1
   :one-pair        2
   :two-pair        3
   :three-of-a-kind 4
   :full-house      5
   :four-of-a-kind  6
   :five-of-a-kind  7
   })

(defn has-pair? [hand]
  (= (count hand) (inc (count (set hand)))))


(defn has-two-pair? [hand]
  (= (count hand) (+ 2 (count (set hand)))))

(defn my-count-map [hand]
  (loop [remaining hand
         result {}]
    (if (empty? remaining)
      result
      (if (contains? result (first remaining))
        (recur (rest remaining) (assoc result (first remaining) (inc (get result (first remaining)))))
        (recur (rest remaining) (assoc result (first remaining) 1))))))

(defn count-map
  "docstring"
  [hand]
  (reduce (fn [result item] (update result item (fnil inc 0))) {} hand)) ; fnil will first try to use inc, but if it's passed in nil, it will give a 0

(defn has-three-of-a-kind? [hand]
  (some #(= 3 %) (vals (count-map hand))))

(defn has-full-house? [hand]
  (and (has-three-of-a-kind? hand) (some #(= 2 %) (vals (count-map hand)))))

(defn has-four-of-a-kind? [hand]
  (some #(= 4 %) (vals (count-map hand))))

(defn has-five-of-a-kind? [hand]
  (some #(= 5 %) (vals (count-map hand))))

(defn calc-hand-type
  "docstring"
  [hand]
  (cond
    (has-five-of-a-kind? hand)
    :five-of-a-kind
    (has-four-of-a-kind? hand)
    :four-of-a-kind
    (has-full-house? hand)
    :full-house
    (has-three-of-a-kind? hand)
    :three-of-a-kind
    (has-two-pair? hand)
    :two-pair
    (has-pair? hand)
    :one-pair
    :else
    :high-card)
  )

(defn get-hand-ranking
  "docstring"
  [hand]
  (hand-ranking-map (calc-hand-type hand)))


(defn convert-to-tie-comparator-number
  "docstring"
  [hand]
  (apply + (map-indexed (fn [index card] (* (Math/pow 20 index) (card-ranking-map card))) (reverse hand))))

(defn compare-rows
  "docstring"
  [row1 row2]
  (let [hand1 (:hand row1)
        hand2 (:hand row2)
        score1 (get-hand-ranking hand1)
        score2 (get-hand-ranking hand2)]
    (cond
      (not= score1 score2)
      (compare score1 score2)
      :else
      (let [tie-score1 (convert-to-tie-comparator-number hand1)
            tie-score2 (convert-to-tie-comparator-number hand2)]
        (compare tie-score1 tie-score2)))))

(defn calc-winnings
  "docstring"
  [rows]
  (apply + (map-indexed
             (fn [index row] (* (inc index) (:bid row)))
             (sort compare-rows rows))))


(convert-to-tie-comparator-number "JJA34")

(def small-input "./src/advent_of_code/day7/small-input.txt")
(def input "./src/advent_of_code/day7/input.txt")

(defn parse-to-row [[hand bid]]
  {:hand hand :bid (parse-long bid)})

(defn calc-winnings-for-input
  "docstring"
  [input]
  (->> input
       slurp
       str/split-lines
       (map #(str/split % #" "))
       (map parse-to-row)
       calc-winnings))

(calc-winnings-for-input small-input)
(calc-winnings-for-input input)


;(compare-rows {:hand "QQQJA", :bid 28})

