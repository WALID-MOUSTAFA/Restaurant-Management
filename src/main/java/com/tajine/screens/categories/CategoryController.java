package com.tajine.screens.categories;

import com.tajine.domain.MealCategory;
import com.tajine.screens.commons.FXController;
import com.tajine.services.MealCategoriesService;
import com.tajine.utils.FXUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class CategoryController extends FXController {
        @Autowired
        private MealCategoriesService mealCategoriesService;

        @Autowired
        private EditCategoryController editCategoryController;

        @FXML
        private TextField categoryName;
        @FXML
        private TableView<MealCategory> categoriesTableView;
        @FXML
        private Button editCategoryButton;
        @FXML
        private Button deleteCategoryButton;

	private List<MealCategory> categories = new ArrayList<>();
	
        @FXML
        private void initialize() {
                this.initializeCategoryTableView();
                this.editCategoryButton.disableProperty().bind(Bindings.isEmpty(this.categoriesTableView.getSelectionModel().getSelectedItems()));
                this.deleteCategoryButton.disableProperty().bind(Bindings.isEmpty(this.categoriesTableView.getSelectionModel().getSelectedItems()));
        }


        @FXML
        private void addCategoryAction() {
                String name;
                MealCategory mealCategory;
		try {
                        mealCategory = new MealCategory();
                        name = this.categoryName.getText();
                        if (name.isEmpty() || name == null) {
                                FXUtils.ALERT_ERROR("يلزم إدخال اسم الفئة");
                                return;
                        }
                        mealCategory.setTitle(name);

                        MealCategory result = this.mealCategoriesService.createCategory(mealCategory);
                        if (result != null) {
                                FXUtils.ALERT_SUCCESS("تمت الإضافة بنجاح.");
                                this.categoriesTableView.getItems().add(result);
                                this.categoryName.clear();
                        }
                } catch (ConstraintViolationException e){
                        FXUtils.ALERT_ERROR("يوجد فئة بهذا الاسم موجودة مسبقاً");
                        e.printStackTrace();
                }
        }

        @FXML
        private void editCategoryAction() throws IOException {
                MealCategory mealCategory = this.categoriesTableView.getSelectionModel().getSelectedItem();
                this.editCategoryController.setTargetMealCategory(mealCategory);
                this.editCategoryController.open(null);
                if(this.editCategoryController.getTargetMealCategory() != null) {
			this.initializeCategoryTableView();
                }

        }

        @FXML
        private void deleteCategoryAction() {

                MealCategory toBeDeleted = this.categoriesTableView.getSelectionModel().getSelectedItem();

		try {
			if(FXUtils.ALERT_CONFIRM("حذف الفئة " + toBeDeleted.getTitle() + "?")){
				this.mealCategoriesService.deleteCategory(toBeDeleted);
				this.categoriesTableView.getItems().remove(toBeDeleted);
			}
		} catch (Exception e) {
			FXUtils.ALERT_ERROR("لا يمكن حذف هذه الفئة لأنها مرتبطة بأصناف موجودة.");
			e.printStackTrace();
			return;
                }

        }

        private void initializeCategoryTableView() {
		this.categoriesTableView.getColumns().clear();
                TableColumn<MealCategory, String> titleColumn = new TableColumn<>("الاسم");
                TableColumn<MealCategory, String> createdAtColumn = new TableColumn<>("وقت الإنشاء");

                titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
                createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
                this.categoriesTableView.getColumns().addAll(titleColumn, createdAtColumn);

                this.asyncSetCategoryTableItems();
        }

        private void asyncSetCategoryTableItems() {
                Task<List<MealCategory>> task = new Task<List<MealCategory>>() {
                        @Override
                        protected List<MealCategory> call() throws Exception {
                                return CategoryController.this.mealCategoriesService.findAllMealCategories();
                        }

                        @Override
                        protected void succeeded() {
                                        CategoryController.this.categoriesTableView.setItems(FXCollections.observableArrayList(this.getValue()));
					CategoryController.this.categories.addAll(this.getValue());
                        }
                };
                
                Thread thread = new Thread(task);
                thread.start();
        }


}
