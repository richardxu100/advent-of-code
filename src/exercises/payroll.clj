(ns exercises.payroll (:require [clojure.test :refer :all]))

(defn parse-date [param1])

(defn payroll [today db])

(defn get-pay-class
  "docstring"
  [employee]
  (first (:pay-class employee)))

(defn get-disposition
  "docstring"
  [paycheck-directive]
  (first (:disposition paycheck-directive)))

(defmulti is-today-payday :schedule)                        ; :schedule is a function call?
(defmulti calc-pay get-pay-class)
(defmulti dispose get-disposition)

(defn get-employees-to-be-paid-today
  "docstring"
  [today employees]
  (filter #(is-today-payday % today) employees))

(defn- build-employee [db employee]
  (assoc employee :db db))

(defn get-employees
  "docstring"
  [db]
  (map (partial build-employee db) (:employees db)))

(defn create-paycheck-directives
  "docstring"
  [ids payments dispositions]
  (map #(assoc {} :id %1 :amount %2 :disposition %3) ids payments dispositions))

(defn send-paychecks
  "docstring"
  [ids payments dispositions]
  (for [paycheck-directive (create-paycheck-directives ids payments dispositions)]
    (dispose paycheck-directive)))

(defn get-paycheck-amounts
  "docstring"
  [employees]
  (map calc-pay
       employees))

(defn get-dispositions
  "docstring"
  [employees]
  (map :disposition employees))

(defn get-ids
  "docstring"
  [employees]
  (map :id employees))

(defn- get-salary [employee]
  (second (:pay-class employee)))

(defmethod calc-pay :salaried [employee]
  (get-salary employee))

(defmethod calc-pay :hourly [employee]
  (let [db (:db employee)
        time-cards (:time-cards db)
        my-time-cards (get time-cards (:id employee))
        [_ hourly-rate] (:pay-class employee)
        hours (map second my-time-cards)
        total-hours (reduce + hours)]
    (* total-hours hourly-rate)))

(defmethod calc-pay :commissioned [employee]
  (let [db (:db employee)
        sales-receipts (:sales-receipts db)
        my-sales-receipts (get sales-receipts (:id employee))
        [_ base-pay commission-rate] (:pay-class employee)
        sales (map second my-sales-receipts)
        total-sales (reduce + sales)]
    (+ (* total-sales commission-rate) base-pay)))

(deftest it-pays-one-salaried-employee-at-end-of=month-by-mail
  ((let [employees [{:id "emp1"
                     :schedule :monthly
                     :pay-class [:salaried 5000]
                     :disposition [:mail "home"]}]
         db {:employees employees}
         today (parse-date "Nov 30 2021")]
     (is (= [{:type :mail
              :id "emp1"
              :name "name"
              :address "home"
              :amount 5000}]
            (payroll today db))))))

(deftest it-pays-one-hourly-employee-on-Friday-by-Direct-Deposit
  ((let [employees [{:id "emp1"
                     :schedule :monthly
                     :pay-class [:hourly 15]
                     :disposition [:deposit "routing" "account"]}]
         time-cards {"empid" [["Nov 12 2022" 80/10]]}
         db {:employees employees :time-cards time-cards}
         friday (parse-date "Nov 18 2022")]
     (is (= [{:type :deposit
              :id "empid"
              :routing "routing"
              :account "account"
              :amount 120}]
            (payroll friday db))))))

(deftest it-pays-one-commissioned-employee-on-an-even-Friday-By-Paymaster
  (let [employees [{:id "empid"
                    :schedule :biweekly
                    :pay-class [:commissioned 100 5/100]
                    :disposition [:paymaster "paymaster"]}]
        sales-receipts {"empid" [["Nov 12 2022" 15000]]}
        db {:employees employees :sales-receipts sales-receipts}
        friday (parse-date "Nov 18 2022")]
    (is (= [{:type :paymaster
             :id "empid"
             :paymaster "paymaster"
             :amount 850}]
           (payroll friday db)))))



