(ns advent-of-code-2024.day1 [:require [clojure.string :as str]])
(+ 2 3)

(def nums_array (str/split (slurp "Desktop/code/clojure/advent-of-code/src/advent_of_code_2024/day1_input.txt") #"\n"))

(def nums_array_split (map (fn [element] (str/split element #" ")) nums_array))

(def num_pairs (map #(filter seq %) nums_array_split))

(def first_list (map (fn [n] (Integer/parseInt n)) (map first num_pairs)))
(def second_list (map (fn [n] (Integer/parseInt n)) (map second num_pairs)))

first_list

second_list

(def sorted_first_list (sort first_list))
(def sorted_second_list (sort second_list))

(defn subtract_lists [list1 list2]
  (loop [remaining_list1 list1
         remaining_list2 list2
         subtracted_list []]
    (if (empty? remaining_list1)
      subtracted_list
      (recur (rest remaining_list1) (rest remaining_list2) (conj subtracted_list (- (first remaining_list1) (first remaining_list2)))))))

(defn new_subtract_lists [list1 list2]
  (for [a list1
        b list2]
    (- a b)))

(def subtracted_list (new_subtract_lists sorted_first_list sorted_second_list))

(apply + (map abs subtracted_list))

; Part 2!
(defn count_map [nums]
  (loop [remaining_nums nums
         current (first remaining_nums)
         result {}]
    (cond
      (empty? remaining_nums)
      result
      (contains? result current)
      (recur (rest remaining_nums) (second remaining_nums) (assoc result current (inc (get result current))))
      :else
      (recur (rest remaining_nums) (second remaining_nums) (assoc result current 1)))))

(def list2_count_map (count_map sorted_second_list))

(defn calc_simularity_score [list1, list_count_map]
  (loop [remaining_items list1
         result 0]
    (if (empty? remaining_items)
      result
      (recur (rest remaining_items) (+ result (* (first remaining_items) (get list_count_map (first remaining_items) 0)))))))

(defn new_calc_simularity_score [list1, list_count_map]
  (apply + (for [number list1
                 :let [simularity_score (* number (get list_count_map number 0))]]
             simularity_score)))
(calc_simularity_score sorted_first_list list2_count_map)
(new_calc_simularity_score sorted_first_list list2_count_map)

; it works! :->


