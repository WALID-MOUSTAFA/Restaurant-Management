package com.tajine.screens.meals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.tajine.domain.Meal;
import com.tajine.domain.MealCategory;
import com.tajine.screens.commons.FXController;
import com.tajine.services.MealCategoriesService;
import com.tajine.services.MealService;
import com.tajine.utils.FXUtils;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

@Component
public class MealController extends FXController {
  @Autowired
  private MealService mealService;
  @Autowired
  private MealCategoriesService mealCategoriesService;
  @Autowired
  private EditMealController editMealController;
  @FXML
  private TableView<Meal> mealsTableView;
  @FXML
  private ComboBox<MealCategory> mealCategoryComboBox;
  @FXML
  private TextField mealTitle;
  @FXML
  private TextField mealPrice;
  @FXML
  private Button deleteMealButton;
  @FXML
  private Button editMealButton;
  @FXML
  private TextField searchField;
  private List<Meal> meals = new ArrayList<>();

  @FXML
  private void initialize() {
    try {
      this.initializeMealsTableView();
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.initializeMealCategoriesComboBox();
    this.editMealButton
      .disableProperty()
      .bind(Bindings.isEmpty(this.mealsTableView.getSelectionModel().getSelectedItems()));
    this.deleteMealButton
      .disableProperty()
      .bind(Bindings.isEmpty(this.mealsTableView.getSelectionModel().getSelectedItems()));

    this.searchField.textProperty().addListener((observable, oldValue, newValue) -> {
	this.doSearch(newValue);
      });
  }

  private void initializeMealCategoriesComboBox() {
    ObservableList<MealCategory> categories =
      FXCollections.observableArrayList(mealCategoriesService.findAllMealCategories());
    this.mealCategoryComboBox.setItems(categories);
    mealCategoryComboBox.setConverter(new StringConverter<MealCategory>() {
	@Override
	public String toString(MealCategory object) {
	  return object.getTitle();
	}

	@Override
	public MealCategory fromString(String string) {
	  return null;
	}
      });
  }

  @FXML
  private void addMealAction() {
    Meal meal;
    String title;
    int price;
    MealCategory category;


    try { //general purpose exception;
      meal = new Meal();

      title = this.mealTitle.getText();
      try {
	price = Integer.valueOf(this.mealPrice.getText());
	meal.setPrice(price);

      } catch (NumberFormatException e) {
	FXUtils.ALERT_ERROR("ادخل البيانات الرقمية بشكل سليم");
	e.printStackTrace();
	return;
      }
      category = this.mealCategoryComboBox.getValue();
      if(category == null) {
	FXUtils.ALERT_ERROR("يجب اختيار فئة");
	return;
      }
      meal.setTitle(title);
      meal.setCategory(category);
      try {
	Meal result = this.mealService.createMeal(meal);
	if(result != null) {
	  FXUtils.ALERT_SUCCESS("تمت الإضافة بنجاح");
	  this.mealsTableView.getItems().add(result);
	}
      } catch (ConstraintViolationException e) {
	FXUtils.ALERT_ERROR("يوجد صنف بهذا الإسم");
	e.printStackTrace();
	return;
      }
		
      this.mealTitle.clear();
      this.mealPrice.clear();
      this.mealCategoryComboBox.getSelectionModel().clearSelection();
    }catch(Exception e) {
      FXUtils.ALERT_ERROR("حدث خطأ أثناء الإضافة، تأكد من البيانات");
      e.printStackTrace();
      return;
    }
  }

  @FXML
  private void editMealAction() throws IOException {
    this.editMealController.setTargetMeal(this.mealsTableView.getSelectionModel().getSelectedItem());
    this.editMealController.open(null);
    this.initializeMealsTableView();
  }

  @FXML
  private void deleteMealAction() {
    Meal toBeDeleted = this.mealsTableView.getSelectionModel().getSelectedItem();
    if (FXUtils.ALERT_CONFIRM("حذف الصنف:  " + toBeDeleted.getTitle() + "?"))
      {
	this.mealService.deleteMeal(toBeDeleted);
	this.mealsTableView.getItems().remove(toBeDeleted);
      }
  }

  private void initializeMealsTableView() {
    this.mealsTableView.getColumns().clear();
    TableColumn<Meal, String> title;
    TableColumn<Meal, String> price;
    TableColumn<Meal, String> createdAt;
    TableColumn<Meal, String> category;

    title = new TableColumn<>("الاسم");
    title.setCellValueFactory(new PropertyValueFactory<>("title"));
    price = new TableColumn<>("السعر");
    price.setCellValueFactory(new PropertyValueFactory<>("price"));
    createdAt = new TableColumn<>("وقت الإنشاء");
    createdAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    category = new TableColumn<>("الفئة");
    category.setCellValueFactory((val)->{
	if(val.getValue().getCategory() == null) {
	  return new SimpleStringProperty("");
	}  else {
	  return new SimpleStringProperty(val.getValue().getCategory().getTitle());
	}
      });

                
    this.mealsTableView.getColumns().addAll(title, price, category, createdAt);
    this.asyncSetMealsTableItems();
  }
	
  private void asyncSetMealsTableItems() {
    Task<List<Meal>> task = new Task<List<Meal>>() {
				
	@Override
	protected List<Meal> call() throws Exception {
	  return MealController.this.mealService.findAllMeals();
	}
				
	@Override
	protected void succeeded() {
	  MealController.this.meals.clear();
	  MealController.this.meals.addAll(this.getValue());
	  MealController.this.mealsTableView
	    .setItems(FXCollections.observableArrayList(this.getValue()));
					
	}
				
      };
    Thread thread = new Thread(task);
    thread.start();
  }


  private void doSearch(String q) {
    //initialize tableView with existed items
    this.mealsTableView.setItems(FXCollections.observableArrayList(this.meals));
    if (q.isEmpty() || q == null) {
      return;
    }
    ObservableList<Meal> items= this.mealsTableView.getItems();
    FilteredList<Meal> filteredList = new FilteredList<>(items);
    this.mealsTableView.setItems(filteredList);
    filteredList.setPredicate(new Predicate<Meal>() {
				
	@Override
	public boolean test(Meal meal) {
	  return meal.getTitle().contains(q);
	}
				
      });
  }


}
