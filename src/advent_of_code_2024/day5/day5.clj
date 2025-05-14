(ns advent-of-code-2024.day5.day5
  (:require
   [clojure.string :as str]
   [clojure.set :as set]))

(def real-input
  "./src/advent_of_code_2024/day5/day5_input.txt")

(defn add-left-rule [left right rules-map]
  (update-in rules-map [right :disallowed-right] (fnil conj #{}) left))

(defn add-right-rule [left right rules-map]
  (update-in rules-map [left :disallowed-left] (fnil conj #{}) right))

(comment (add-left-rule 5 10 {}))
(comment (add-left-rule 5 10 {10 {:disallowed-right #{13 29}}}))

(defn add-rules [rules-map rule]
  (let [[left right] (map parse-long (str/split rule #"\|"))]
    (->> rules-map
         (add-left-rule left right)
         (add-right-rule left right))))

;(add-rules {} "58|32")

(defn build-rules-map [rules]
  (loop [rules-map {}
         remaining-rules rules]
    (if (empty? remaining-rules)
      rules-map
      (recur (add-rules rules-map (first remaining-rules)) (rest remaining-rules)))))

;(def ex-rules-map (build-rules-map ["58|32" "12|23" "32|51" "10|5"]))

(defn parse-updates [input]
  (->> input
       slurp
       str/split-lines
       (split-with seq)
       (second)
       (filter seq)
       (map #(str/split % #","))
       (map (partial map parse-long))))

(defn parse-page-ordering-rules [input]
  (->> input
       slurp
       str/split-lines
       (split-with seq)
       first
       build-rules-map))

;(parse-page-ordering-rules real-input)
; (parse-updates real-input)

(defn has-valid-left-els [left-els current page-ordering-rules]
  (let [disallowed-left-els (get-in page-ordering-rules [current :disallowed-left] #{})]
    (empty? (set/intersection (set left-els) disallowed-left-els))))

(defn has-valid-right-els [right-els current page-ordering-rules]
  (let [disallowed-right-els (get-in page-ordering-rules [current :disallowed-right] #{})]
    (empty? (set/intersection (set right-els) disallowed-right-els))))

;(has-valid-left-els #{5} 10 ex-rules-map)

(defn is-ordered-update? [page-ordering-rules update-line]
  (loop [left-els []
         right-els (rest update-line)
         current (first update-line)]
    (let [valid-left? (has-valid-left-els left-els current page-ordering-rules)
          valid-right? (has-valid-right-els right-els current page-ordering-rules)]
      (if (empty? right-els)
        valid-left?
        (if (not (and valid-left? valid-right?))
          false
          (recur (conj left-els current) (rest right-els) (first right-els)))))))

;(is-ordered-update? ex-rules-map [10 23 5 123])

(defn find-ordered-updates [input]
  (let [updates (parse-updates input)
        page-ordering-rules (parse-page-ordering-rules input)]
    (filter #(is-ordered-update? page-ordering-rules %) updates)))

(defn take-middle-element [coll]
  (nth coll (/ (count coll) 2)))

(defn part1 [input]
  (->> input
       find-ordered-updates
       (map take-middle-element)
       (reduce +)))

(part1 real-input)

(find-ordered-updates real-input)

;(def fake-input "./src/advent_of_code_2024/day5/test_input.txt")

;(find-ordered-updates fake-input)

;; part2

(defn fix-unordered-update [page-ordering-rules update-line]
  (sort (fn [a b] (if (contains? (get-in page-ordering-rules [a :disallowed-left]) b)
                     1
                     -1)) update-line))

;(defn ex-sort
;  "docstring"
;  [nums]
;  (sort > nums))
;
;(defn ex-custom-sort
;  "docstring"
;  [nums]
;  (sort (fn [a b] (if (> a b)
;                   1
;                   -1))) nums)
;
;(ex-sort '(32 1 2 12 94))
;(ex-custom-sort '(32 1 2 12 94))
(def ex-rules {10 {:disallowed-left #{5} :disallowed-right #{30}} 5 {:disallowed-right #{10} :disallowed-left #{30}}})

(fix-unordered-update ex-rules '(5 10)) ;; todo: this method is messed up
(defn fix-unordered-updates [input]
  (let [updates (parse-updates input)
        page-ordering-rules (parse-page-ordering-rules input)
        unordered-updates (filter (complement #(is-ordered-update? page-ordering-rules %)) updates)]
    (map (partial fix-unordered-update page-ordering-rules) unordered-updates)))


(defn find-unordered-updates [input]
  (let [updates (parse-updates input)
        page-ordering-rules (parse-page-ordering-rules input)
        unordered-updates (filter (complement #(is-ordered-update? page-ordering-rules %)) updates)]
    unordered-updates))

(fix-unordered-updates real-input)
(find-unordered-updates real-input)

(parse-page-ordering-rules real-input)
(parse-updates real-input)

(defn part2 [input]
  (->> input
       fix-unordered-updates
       (map take-middle-element)
       (reduce +)))

(part2 real-input)