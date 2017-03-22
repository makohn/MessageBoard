package de.htwsaar.wirth.client.controller;

import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

/**
 * Dieses SelectionModel erlaubt es den Items einer ListView nicht selektiert zu werden.
 */
public class NoSelectionModel extends MultipleSelectionModel<Message> {
	@Override
	public void selectPrevious() {
	}

	@Override
	public void selectNext() {
	}

	@Override
	public void select(Message arg0) {
	}

	@Override
	public void select(int arg0) {
	}

	@Override
	public boolean isSelected(int arg0) {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void clearSelection(int arg0) {
	}

	@Override
	public void clearSelection() {
	}

	@Override
	public void clearAndSelect(int arg0) {
	}

	@Override
	public void selectLast() {
	}

	@Override
	public void selectIndices(int index, int... indices) {
	}

	@Override
	public void selectFirst() {
	}

	@Override
	public void selectAll() {
	}

	@Override
	public ObservableList<Message> getSelectedItems() {
		return FXCollections.emptyObservableList();
	}

	@Override
	public ObservableList<Integer> getSelectedIndices() {
		return FXCollections.emptyObservableList();
	}
}
