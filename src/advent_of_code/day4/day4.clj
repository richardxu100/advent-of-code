(ns advent-of-code.day4.day4
  (:require [clojure.string :as str]))

(defn- parse-numbers
  "docstring"
  [number-string]
  (map parse-long (filter seq (str/split number-string #" "))))

(defn calc-points [card]
  (let [[_ remaining] (str/split card #":")
        [winning-nums-string my-nums-string] (str/split remaining #"\|")
        winning-nums (set (parse-numbers winning-nums-string))
        my-nums (parse-numbers my-nums-string)
        num-matches (count (filter true? (map (fn [my-num] (contains? winning-nums my-num)) my-nums)))]
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

(defn calc-num-matches [card]
  (let [[_ remaining] (str/split card #":")
        [winning-nums-string my-nums-string] (str/split remaining #"\|")
        winning-nums (set (parse-numbers winning-nums-string))
        my-nums (parse-numbers my-nums-string)
        ]
    (count (filter true? (map (fn [my-num] (contains? winning-nums my-num)) my-nums)))))

(defn add-card-number-to-cards-map [num-cards-map card-number]
  (if (contains? num-cards-map card-number)
    (assoc num-cards-map card-number (inc (get num-cards-map card-number)))
    (assoc num-cards-map card-number 1)))

(defn add-cards-to-card-map [card-number num-matches result]
  (loop [remaining num-matches
         updated-result result
         current card-number]
    (if (zero? remaining)
      updated-result
      (recur (dec remaining) (add-card-number-to-cards-map updated-result (inc current)) (inc current)))))

(defn add-card-to-num-cards-map [num-cards-map card-number-with-matches]
  (println card-number-with-matches)
  (let [num-matches (get card-number-with-matches :num-matches)
        card-number (get card-number-with-matches :card-number)
        owned-number (if (contains? num-cards-map card-number)
                       (inc (get num-cards-map card-number))
                       1)]
    (loop [remaining-iterations owned-number
           result (add-card-number-to-cards-map num-cards-map card-number)]
      (if (zero? remaining-iterations)
        result
        (recur (dec remaining-iterations) (add-cards-to-card-map card-number num-matches result))))))


(defn generate-num-cards-map [card-numbers-with-matches]
  (loop [num-cards-map {}
         remaining card-numbers-with-matches]
    (if (empty? remaining)
      num-cards-map
      (recur (add-card-to-num-cards-map num-cards-map (first remaining)) (rest remaining)))))



(let [cards (->
              "./src/advent_of_code/day4/cards.txt"
              slurp
              (str/split #"\n")
              )]
  (->>
    cards
    (map-indexed (fn [index card] {:card-number (inc index) :num-matches (calc-num-matches card)}))
    generate-num-cards-map
    vals
    (apply +)
    ))